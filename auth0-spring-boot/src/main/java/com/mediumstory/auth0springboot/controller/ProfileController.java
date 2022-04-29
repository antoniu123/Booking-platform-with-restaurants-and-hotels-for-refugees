package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.HotelDto;
import com.mediumstory.auth0springboot.dto.RestaurantDto;
import com.mediumstory.auth0springboot.model.Profile;
import com.mediumstory.auth0springboot.repository.ProfileRepository;
import com.mediumstory.auth0springboot.service.JwtService;
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
@RequestMapping("/profile")
public class ProfileController{
	private final ProfileRepository profileRepository;

	private final JwtService jwtService;

	@Autowired
	public ProfileController(final ProfileRepository profileRepository, final JwtService jwtService) {
		this.profileRepository = profileRepository;
		this.jwtService = jwtService;
	}

	@GetMapping(value="/my", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Profile> getProfile(){
		return ResponseEntity.ok().body(profileRepository.findByUserId(jwtService.getUserId()).orElseGet(Profile::new));
	}

	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Profile> saveRestaurant(@RequestBody Profile profile) {
		profile.setUserId(jwtService.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(profileRepository.save(profile));
	}
}
