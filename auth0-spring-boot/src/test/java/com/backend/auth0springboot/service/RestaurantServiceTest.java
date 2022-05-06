package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.RestaurantDto;
import com.backend.auth0springboot.model.Restaurant;
import com.backend.auth0springboot.repository.RestaurantRepository;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RestaurantServiceTest {

	@MockBean
	private RestaurantRepository restaurantRepository;

	private RestaurantService restaurantService;

	@Before
	public void setUp() {
		restaurantService = new RestaurantService(restaurantRepository);
	}

	@Test
	public void shouldGetAllRestaurants() {
		final Restaurant restaurant1 = new Restaurant(1L, "name1", "image1");
		final Restaurant restaurant2 = new Restaurant(2L, "name2", "image2");
		final ImmutableList<Restaurant> listOfRestaurants = ImmutableList.of(restaurant1, restaurant2);
		when(restaurantRepository.findAll()).thenReturn(listOfRestaurants);
		List<RestaurantDto> myList = restaurantService.getAllRestaurants();
		Assert.assertEquals(2, myList.size());
	}

	@Test
	public void shouldGetEmptyListOfRestaurants() {
		final ImmutableList<Restaurant> listOfRestaurants = ImmutableList.of();
		when(restaurantRepository.findAll()).thenReturn(listOfRestaurants);
		List<RestaurantDto> myList = restaurantService.getAllRestaurants();
		Assert.assertEquals(0, myList.size());
	}

	@Test
	public void shouldGetRestaurantById() {
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		final Optional<Restaurant> actual = Optional.of(restaurant);
		when(restaurantRepository.findById(anyLong())).thenReturn(actual);
		RestaurantDto restaurantDto = restaurantService.getRestaurantById(1L);
		Assert.assertEquals(1L, restaurantDto.getId().longValue());
	}

	@Test
	public void shouldNotGetRestaurantById() {
		final Optional<Restaurant> actual = Optional.empty();
		when(restaurantRepository.findById(anyLong())).thenReturn(actual);
		RestaurantDto restaurantDto = restaurantService.getRestaurantById(1L);
		Assert.assertEquals(new RestaurantDto(), restaurantDto);
	}

	@Test
	public void shouldSaveRestaurant(){
		final Restaurant restaurant = new Restaurant(null, "name1", "image1");
		final Restaurant restaurantNew = new Restaurant(1L, "name1", "image1");
		final RestaurantDto restaurantExpectedDto = new RestaurantDto(1L, "name1", "image1");
		when(restaurantRepository.save(restaurant)).thenReturn(restaurantNew);
		RestaurantDto restaurantActualDto = restaurantService.saveRestaurant(new RestaurantDto(null,restaurant.getName(),restaurant.getImage()));
		Assert.assertEquals(restaurantExpectedDto,restaurantActualDto);
	}

	@Test
	public void shouldNotSaveRestaurant(){
		final Restaurant restaurant = new Restaurant(null, "name1", "image1");
		when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
		RestaurantDto restaurantActualDto = restaurantService.saveRestaurant(new RestaurantDto(null,restaurant.getName(),restaurant.getImage()));
		Assert.assertNull(restaurantActualDto.getId());
	}

	@Test
	public void shouldDeleteRestaurant(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
		doNothing().when(restaurantRepository).delete(restaurant);
		restaurantService.deleteRestaurant(1L);
		verify(restaurantRepository).delete(restaurant);
	}

	@Test(expected = JpaSystemException.class)
	public void shouldNotDeleteRestaurant(){
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
		doThrow(new JpaSystemException(new RuntimeException("error at delete"))).when(restaurantRepository).delete(restaurant);
		restaurantService.deleteRestaurant(1L);
	}
}
