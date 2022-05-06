package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.ReservationDto;
import com.backend.auth0springboot.model.Hotel;
import com.backend.auth0springboot.model.Reservation;
import com.backend.auth0springboot.repository.HotelRepository;
import com.backend.auth0springboot.repository.ReservationRepository;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReservationServiceTest {
	@MockBean
	private ReservationRepository reservationRepository;
	@MockBean
	private HotelRepository hotelRepository;
	@MockBean
	private JwtService jwtService;

	private ReservationService reservationService;

	@Before
	public void setup(){
		this.reservationService = new ReservationService(reservationRepository,hotelRepository,jwtService);
	}

	@Test
	public void shouldGetReservation(){
		ZoneId defaultZoneId = ZoneId.systemDefault();
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		Date localDate1 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Date localDate2 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		when(jwtService.getRoles()).thenReturn(Collections.singletonList("ADMIN"));
		when(reservationRepository.findAll()).thenReturn(ImmutableList.of(new Reservation(1L,hotel,localDate1,localDate2,1,"user1"),
				new Reservation(2L,hotel,localDate1,localDate2,0,"user1") ));
		Assert.assertEquals(1, reservationService.getReservations().size());
	}

	@Test
	public void shouldNotGetReservation(){
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		when(jwtService.getRoles()).thenReturn(Collections.singletonList("ADMIN"));
		when(reservationRepository.findAll()).thenReturn(ImmutableList.of());
		Assert.assertEquals(0, reservationService.getReservations().size());
	}

	@Test
	public void shouldSaveReservation(){
		ZoneId defaultZoneId = ZoneId.systemDefault();
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		Date localDate1 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Date localDate2 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Reservation reservation = new Reservation(null,hotel,localDate1,localDate2,1,"user1");
		Reservation reservationExpected = new Reservation(1L,hotel,localDate1,localDate2,1,"user1");
		when(jwtService.getUserId()).thenReturn("user1");
		when(hotelRepository.findHotelByName("hotel1")).thenReturn(hotel);
		when(reservationRepository.save(reservation)).thenReturn(reservationExpected);
		ReservationDto reservationActualDto = reservationService.saveReservation(new ReservationDto(reservation.getId(),reservation.getHotel().getName(),reservation.getDateIn(),reservation.getDateOut(),
				reservation.getValid(),reservation.getUserId()));
		Assert.assertEquals(new ReservationDto(reservationExpected.getId(),reservationExpected.getHotel().getName(),reservationExpected.getDateIn(),
				reservationExpected.getDateOut(),reservationExpected.getValid(),reservationExpected.getUserId()),reservationActualDto);
	}

	@Test(expected = JpaSystemException.class)
	public void shouldNotSaveReservation(){
		ZoneId defaultZoneId = ZoneId.systemDefault();
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		Date localDate1 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Date localDate2 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Reservation reservation = new Reservation(null,hotel,localDate1,localDate2,1,"user1");
		when(reservationRepository.save(reservation)).thenThrow(new JpaSystemException(new RuntimeException("error at save")));
		when(jwtService.getUserId()).thenReturn("user1");
		when(hotelRepository.findHotelByName("hotel1")).thenReturn(hotel);
		reservationService.saveReservation(new ReservationDto(reservation.getId(),reservation.getHotel().getName(),reservation.getDateIn(),reservation.getDateOut(),
				reservation.getValid(),reservation.getUserId()));
	}

	@Test
	public void shouldCancelReservation(){
		ZoneId defaultZoneId = ZoneId.systemDefault();
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		Date localDate1 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Date localDate2 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Reservation reservation = new Reservation(1L,hotel,localDate1,localDate2,1,"user1");
		when(reservationRepository.getById(1L)).thenReturn(reservation);
		Reservation reservationCalled = new Reservation(1L,hotel,localDate1,localDate2,0,"user1");
		reservationService.cancelReservation(1L);
		verify(reservationRepository).save(reservationCalled);
	}

	@Test(expected = JpaSystemException.class)
	public void shouldCancelReservationWithException(){
		ZoneId defaultZoneId = ZoneId.systemDefault();
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		Date localDate1 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Date localDate2 = Date.from(LocalDate.now().atStartOfDay(defaultZoneId).toInstant());
		Reservation reservation = new Reservation(1L,hotel,localDate1,localDate2,1,"user1");
		when(reservationRepository.getById(1L)).thenReturn(reservation);
		Reservation reservationCalled = new Reservation(1L,hotel,localDate1,localDate2,0,"user1");
		doThrow(new JpaSystemException(new RuntimeException("cancel error"))).when(reservationRepository).save(reservationCalled);
		reservationService.cancelReservation(1L);
	}
}
