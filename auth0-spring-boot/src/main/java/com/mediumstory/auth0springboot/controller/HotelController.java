package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.HotelDto;
import com.mediumstory.auth0springboot.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/hotels")
public class HotelController {
	private final HotelService hotelService;

	@Autowired
	public HotelController(final HotelService hotelService) {
		this.hotelService = hotelService;
	}

	@GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HotelDto>> getHotels(){
		return ResponseEntity.ok().body(hotelService.findAllHotels());
	}

	@PostMapping(value = "/hotel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDto> saveHotel(@RequestBody HotelDto hotelDto) throws SQLException {
		return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.saveHotel(hotelDto));
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> deleteHotel(@PathVariable Long id) throws SQLException {
		hotelService.deleteHotel(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(hotelService.findOneHotelById(id));
	}
}
