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
				.map(t-> new HelpPointDto(t.getId(), t.getName(), t.getAddress())).collect(Collectors.toList());
	}

	public HelpPointDto saveHelpPoint(HelpPointDto helpPointDto){
		HelpPoint helpPoint = new HelpPoint(helpPointDto.getId(),helpPointDto.getName(),helpPointDto.getAddress());
		HelpPoint helpPointNew = helpPointRepository.save(helpPoint);
		return new HelpPointDto(helpPointNew.getId(),helpPointNew.getName(),helpPointNew.getAddress());
	}

}
