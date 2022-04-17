package com.mediumstory.auth0springboot.service;

import com.mediumstory.auth0springboot.dto.HelpPointDto;
import com.mediumstory.auth0springboot.model.HelpPoint;
import com.mediumstory.auth0springboot.repository.HelpPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HelpPointService {

	private final HelpPointRepository helpPointRepository;

	@Autowired
	public HelpPointService(final HelpPointRepository helpPointRepository) {
		this.helpPointRepository = helpPointRepository;
	}

	public List<HelpPointDto> findAllHelpPoints(){
		return helpPointRepository.findAll().stream()
				.map(t-> new HelpPointDto(t.getId(), t.getName(), t.getLongitude(), t.getLatitude(), t.getAddress())).collect(Collectors.toList());
	}

}
