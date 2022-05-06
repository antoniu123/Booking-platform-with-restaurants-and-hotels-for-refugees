package com.backend.auth0springboot.controller;

import com.backend.auth0springboot.dto.HotelDto;
import com.backend.auth0springboot.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
public class HotelControllerTest {

	@MockBean
	private HotelService hotelService;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@Before
	public void setUp(){
		objectMapper = new ObjectMapper();
		mockMvc = standaloneSetup(new HotelController(hotelService)).build();
	}

	@Test
	public void shouldReponseOkOnFindAllHotels() throws Exception {
		when(hotelService.findAllHotels()).thenReturn(getHotelList());

		final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/all"))
				.andExpect(status().isOk())
				.andReturn();

		final List<HotelDto> response = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), HotelDto[].class));

		Assert.assertEquals(2, response.size());

	}

	private List<HotelDto> getHotelList(){
		HotelDto hotelDto1 = new HotelDto(1L, "hotel1", "zona1", 10, "image1.jpg");
		HotelDto hotelDto2 = new HotelDto(2L, "hotel2", "zona2", 20, "image2.jpg");
		return ImmutableList.of(hotelDto1, hotelDto2);
	}
}
