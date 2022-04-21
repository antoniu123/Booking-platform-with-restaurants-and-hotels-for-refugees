package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.MenuRestaurantDto;
import com.mediumstory.auth0springboot.dto.RestaurantDto;
import com.mediumstory.auth0springboot.model.MenuRestaurant;
import com.mediumstory.auth0springboot.service.MenuRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/menuRestaurant")
public class MenuItemController {
	private final MenuRestaurantService menuRestaurantService;

	@Autowired
	public MenuItemController(final MenuRestaurantService menuRestaurantService) {
		this.menuRestaurantService = menuRestaurantService;
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<MenuRestaurantDto>> getReservations(@PathVariable Long id) throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(menuRestaurantService.getAllMenuItems(id));
	}
}
