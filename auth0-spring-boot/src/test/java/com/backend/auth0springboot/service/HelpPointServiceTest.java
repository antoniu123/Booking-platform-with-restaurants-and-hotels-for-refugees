package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.HelpPointDto;
import com.backend.auth0springboot.model.HelpPoint;
import com.backend.auth0springboot.repository.HelpPointRepository;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class HelpPointServiceTest {
	@MockBean
	private HelpPointRepository helpPointRepository;

	private HelpPointService helpPointService;

	@Before
	public void setUp() {
		helpPointService = new HelpPointService(helpPointRepository);
	}

	@Test
	public void shouldReturnListOfHelpPoints(){
		when(helpPointRepository.findAll())
				.thenReturn(ImmutableList.of(new HelpPoint(1L,"name1","address1"),
						new HelpPoint(2L,"name2","address2")));
		Assert.assertEquals(2, helpPointService.findAllHelpPoints().size());
	}

	@Test
	public void shouldReturnEmptyListOfHelpPoints(){
		when(helpPointRepository.findAll())
				.thenReturn(ImmutableList.of());
		Assert.assertEquals(0, helpPointService.findAllHelpPoints().size());
	}

	@Test
	public void shouldSaveHelpPoint(){
		final HelpPoint helpPoint = new HelpPoint(1L, "name1", "address1");
		when(helpPointRepository.save(helpPoint)).thenReturn(helpPoint);
		Assert.assertEquals(new HelpPointDto(helpPoint.getId(),helpPoint.getName(),helpPoint.getAddress()),
				helpPointService.saveHelpPoint(new HelpPointDto(helpPoint.getId(),helpPoint.getName(),helpPoint.getAddress())));
	}

	@Test(expected = JpaSystemException.class)
	public void shouldThrowExceptionWhenSaveHelpPoint(){
		final HelpPoint helpPoint = new HelpPoint(1L, "name1", "address1");
		when(helpPointRepository.save(helpPoint)).thenThrow(new JpaSystemException(new RuntimeException("save error")));
		helpPointService.saveHelpPoint(new HelpPointDto(helpPoint.getId(),helpPoint.getName(),helpPoint.getAddress()));
	}
}
