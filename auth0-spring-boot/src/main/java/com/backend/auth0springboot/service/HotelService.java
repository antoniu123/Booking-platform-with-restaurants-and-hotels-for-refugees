package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.HotelDto;
import com.backend.auth0springboot.model.Hotel;
import com.backend.auth0springboot.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService{

	private final HotelRepository hotelRepository;

	@Autowired
	public HotelService(final HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	public List<HotelDto> findAllHotels(){
		return hotelRepository.findAll().stream()
				.map(t-> new HotelDto(t.getId(), t.getName(), t.getZone(), t.getNr_rooms(), t.getImage()))
				.collect(Collectors.toList());
	}

	public HotelDto saveHotel(HotelDto hotelDto){
		Hotel hotel = getHotelFromHotelDto(hotelDto);
		Hotel hotelNew = hotelRepository.save(hotel);
		return new HotelDto(hotelNew.getId(), hotelDto.getName(), hotelDto.getZone(), hotelDto.getNr_rooms(),hotelDto.getImage());
	}

	public HotelDto findOneHotelById(Long id){
		Hotel hotel = hotelRepository.findById(id).orElseGet(Hotel::new);
		return new HotelDto(hotel.getId(), hotel.getName(),hotel.getZone(),hotel.getNr_rooms(), hotel.getImage());
	}

	public void deleteHotel(Long hotelId){
		Hotel hotel = hotelRepository.findById(hotelId).orElseGet(Hotel::new);
		hotelRepository.delete(hotel);
	}

	public HotelDto getHotelDtoFromHotel(Hotel hotel){
		return new HotelDto(hotel.getId(), hotel.getName(), hotel.getZone(), hotel.getNr_rooms(), hotel.getImage());
	}

	public Hotel getHotelFromHotelDto(HotelDto hotelDto){
		return new Hotel(hotelDto.getId(), hotelDto.getName(), hotelDto.getZone(), hotelDto.getNr_rooms(), hotelDto.getImage());
	}
}
