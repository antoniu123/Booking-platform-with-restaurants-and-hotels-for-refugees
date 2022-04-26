package com.mediumstory.auth0springboot.controller;


import com.mediumstory.auth0springboot.dto.RestaurantDto;
import com.mediumstory.auth0springboot.service.RestaurantService;
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
@RequestMapping("/restaurants")
public class RestaurantController {
	private final RestaurantService restaurantService;

	@Autowired
	public RestaurantController(final RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RestaurantDto>> getReservations() throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getAllRestaurants());
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestaurantDto> getHotelById(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantById(id));
	}

	@PostMapping(value = "/restaurant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestaurantDto> saveRestaurant(@RequestBody RestaurantDto restaurantDto) throws SQLException {
		return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.saveRestaurant(restaurantDto));
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) throws SQLException {
		restaurantService.deleteRestaurant(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
