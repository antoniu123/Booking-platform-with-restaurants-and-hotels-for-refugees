package com.mediumstory.auth0springboot.service;

import com.mediumstory.auth0springboot.dto.HotelDto;
import com.mediumstory.auth0springboot.model.Hotel;
import com.mediumstory.auth0springboot.repository.HotelRepository;
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
		Hotel hotel = new Hotel(hotelDto.getId(), hotelDto.getName(), hotelDto.getZone(), hotelDto.getNr_rooms(),hotelDto.getImage());
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
}
