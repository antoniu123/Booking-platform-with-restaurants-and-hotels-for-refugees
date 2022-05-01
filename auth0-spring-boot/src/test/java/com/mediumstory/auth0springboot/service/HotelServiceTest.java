package com.mediumstory.auth0springboot.service;

import com.google.common.collect.ImmutableList;
import com.mediumstory.auth0springboot.dto.HotelDto;
import com.mediumstory.auth0springboot.model.Hotel;
import com.mediumstory.auth0springboot.repository.HotelRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class HotelServiceTest {

	@MockBean
	private HotelRepository hotelRepository;

	private HotelService hotelService;

	@Before
	public void setUp() {
		hotelService = new HotelService(hotelRepository);
	}

	@Test
	public void shouldReturnListOfHotels() {
		when(hotelRepository.findAll()).thenReturn(
				ImmutableList.of(new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg"),
						new Hotel(2L, "hotel2", "zona2", 10, "image2.jpg")
				));
		Assert.assertEquals(2, hotelService.findAllHotels().size());
	}

	@Test
	public void shouldSaveHotel() {
		final HotelDto hotelBefore = new HotelDto(null, "hotel1", "zona1", 10, "image1.jpg");
		when(hotelRepository.save(new Hotel(hotelBefore.getId(), hotelBefore.getName(), hotelBefore.getZone(),
				hotelBefore.getNr_rooms(), hotelBefore.getImage())))
				.thenReturn(new Hotel(1L, hotelBefore.getName(), hotelBefore.getZone(),
						hotelBefore.getNr_rooms(), hotelBefore.getImage()));
		final HotelDto hotelAfter = new HotelDto(1L, "hotel1", "zona1", 10, "image1.jpg");
		Assert.assertEquals(hotelAfter, hotelService.saveHotel(hotelBefore));
	}

	@Test
	public void shouldThrowExceptionWhenSavingHotel() {
		HotelService hotelServiceSpy = Mockito.spy(hotelService);
		final HotelDto hotelDto = new HotelDto(1L, "hotel1", "zona1", 10, "image1.jpg");
		final Hotel hotel = new Hotel(1L, "hotel1", "zona1", 10, "image1.jpg");
		when(hotelRepository.save(hotel)).thenReturn(hotel);
		doReturn(hotel).when(hotelServiceSpy).getFromHotelDto(hotelDto);

		HotelDto hotelDtoNew = hotelService.saveHotel(hotelDto);
		Assert.assertEquals(hotelDtoNew, hotelDto);
	}
}
