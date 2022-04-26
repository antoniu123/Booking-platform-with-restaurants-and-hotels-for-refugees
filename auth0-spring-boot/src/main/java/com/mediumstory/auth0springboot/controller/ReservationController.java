package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.ReservationDto;
import com.mediumstory.auth0springboot.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/reservations")
public class ReservationController {
	private final ReservationService reservationService;

	@Autowired
	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping(value = "/reservation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReservationDto> saveReservation(@RequestBody ReservationDto reservationDto) throws SQLException {
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.saveReservation(reservationDto));
	}

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ReservationDto>> getReservations() throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservations());
	}
}
