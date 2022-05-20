package com.backend.auth0springboot.integration;

import com.backend.auth0springboot.Auth0SpringBootApplication;
import com.backend.auth0springboot.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Auth0SpringBootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

	@LocalServerPort
	public String port;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.apply(springSecurity())
				.build();
	}

	@Test
	public void shouldReponseOkBecauseNotSecurity() throws Exception {
		final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/auth0/public"))
				.andExpect(status().isOk())
				.andReturn();

		final ResponseDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ResponseDto.class);

		Assert.assertEquals("Public Endpoint Working fine !", response.getMessage());

	}

}
