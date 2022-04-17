package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.HelpPointDto;
import com.mediumstory.auth0springboot.service.HelpPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/help")
public class HelpPointController {
	@Autowired
	private HelpPointService helpPointService;

	public HelpPointController(HelpPointService helpPointService) {
		this.helpPointService = helpPointService;
	}

	@GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HelpPointDto>> getHelpPoints(){
		return ResponseEntity.ok().body(helpPointService.findAllHelpPoints());
	}

}
