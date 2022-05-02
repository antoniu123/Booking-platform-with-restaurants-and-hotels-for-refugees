package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.ReservationDto;
import com.backend.auth0springboot.model.Reservation;
import com.backend.auth0springboot.repository.HotelRepository;
import com.backend.auth0springboot.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;

	private final HotelRepository hotelRepository;

	private final JwtService jwtService;

	@Autowired
	public ReservationService(final ReservationRepository reservationRepository,
							  final HotelRepository hotelRepository, JwtService jwtService) {
		this.reservationRepository = reservationRepository;
		this.hotelRepository = hotelRepository;
		this.jwtService = jwtService;
	}

	public ReservationDto saveReservation(ReservationDto reservationDto){

		Reservation reservation = new Reservation(reservationDto.getId(), hotelRepository.findHotelByName(reservationDto.getHotelName()),
													reservationDto.getDateIn(),reservationDto.getDateOut(),
													reservationDto.getValid(), jwtService.getUserId());
		Reservation reservationNew = reservationRepository.save(reservation);

		return new ReservationDto(reservationNew.getId(), reservationNew.getHotel().getName(),
							reservationNew.getDateIn(),reservationNew.getDateOut(),
							reservationNew.getValid(),reservationNew.getUserId());
	}

	public List<ReservationDto> getReservations(){
		List<String>  roles = jwtService.getRoles();
		if (roles.contains("ADMIN")){
			return reservationRepository.findAll().stream().filter(r->r.getValid().equals(1))
					.map(reservation->new ReservationDto(reservation.getId(),
							reservation.getHotel().getName(), reservation.getDateIn(), reservation.getDateOut(),
							reservation.getValid(), reservation.getUserId()))
					.collect(Collectors.toList());
		}
		else {
			return reservationRepository.findAll().stream()
					.filter(r->r.getValid().equals(1) && r.getUserId().equals(jwtService.getUserId()))
					.map(reservation->new ReservationDto(reservation.getId(),
							reservation.getHotel().getName(), reservation.getDateIn(), reservation.getDateOut(),
							reservation.getValid(), reservation.getUserId()))
					.collect(Collectors.toList());
		}
	}

	@Transactional
	public void cancelReservation(Long id) {
		final Reservation reservation = reservationRepository.getById(id);
		reservation.setValid(0);
		reservationRepository.save(reservation);
	}
}
