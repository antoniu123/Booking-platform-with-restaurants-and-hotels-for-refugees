package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.MenuRestaurantDto;
import com.backend.auth0springboot.model.MenuRestaurant;
import com.backend.auth0springboot.model.Restaurant;
import com.backend.auth0springboot.repository.MenuRestaurantRepository;
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
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MenuRestaurantServiceTest {
	@MockBean
	private MenuRestaurantRepository menuRestaurantRepository;
	@MockBean
	private RestaurantRepository restaurantRepository;

	private MenuRestaurantService menuRestaurantService;

	@Before
	public void setup() {
		menuRestaurantService = new MenuRestaurantService(menuRestaurantRepository, restaurantRepository);
	}

	@Test
	public void shouldReturnListOfMenuItemsFromAllRestaurants() {
		when(menuRestaurantRepository.findAll()).thenReturn(ImmutableList.of(new MenuRestaurant(1L, "name1", new Restaurant(1L, "name1", "image1"), 1f, "image1"),
				new MenuRestaurant(2L, "name2", new Restaurant(1L, "name1", "image1"), 1f, "image2")));
		Assert.assertEquals(2, menuRestaurantService.getAllMenuItemsFromAllRestaurants().size());
	}

	@Test
	public void shouldReturnEmptyListOfMenuItemsFromAllRestaurants() {
		when(menuRestaurantRepository.findAll()).thenReturn(ImmutableList.of());
		Assert.assertEquals(0, menuRestaurantService.getAllMenuItemsFromAllRestaurants().size());
	}

	@Test
	public void shouldReturnListOfMenuItemsFromRestaurantId() {
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRestaurantRepository.findAllByRestaurant(restaurant)).thenReturn(ImmutableList.of(new MenuRestaurant(1L, "name1", new Restaurant(1L, "name1", "image1"), 1f, "image1"),
				new MenuRestaurant(2L, "name2", new Restaurant(1L, "name1", "image1"), 1f, "image2")));
		List<MenuRestaurantDto> menuRestaurantDtoList = menuRestaurantService.getMenuForRestaurantId(1L);
		Assert.assertEquals(2, menuRestaurantDtoList.size());
		Assert.assertEquals(ImmutableList.of(1L, 2L), menuRestaurantDtoList.stream().map(MenuRestaurantDto::getId).collect(Collectors.toList()));
	}

	@Test
	public void shouldReturnEmptyListOfMenuItemsWithoutRestaurant() {
		when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());
		List<MenuRestaurantDto> menuRestaurantDtoList = menuRestaurantService.getMenuForRestaurantId(1L);
		Assert.assertTrue(menuRestaurantDtoList.isEmpty());
	}

	@Test
	public void shouldReturnEmptyListOfMenuItemsWithRestaurantButNoMenu() {
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
		when(menuRestaurantRepository.findAllByRestaurant(restaurant)).thenReturn(ImmutableList.of());
		List<MenuRestaurantDto> menuRestaurantDtoList = menuRestaurantService.getMenuForRestaurantId(1L);
		Assert.assertTrue(menuRestaurantDtoList.isEmpty());
	}

	@Test
	public void shouldReturnMenuForId() {
		final MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name1", new Restaurant(1L, "name1", "image1"), 1f, "image1");
		when(menuRestaurantRepository.findById(1L)).thenReturn(Optional.of
				(menuRestaurant));
		final MenuRestaurantDto menuRestaurantDto = new MenuRestaurantDto(menuRestaurant.getId(), menuRestaurant.getName(),
				menuRestaurant.getRestaurant().getName(), menuRestaurant.getPrice(), menuRestaurant.getImage());
		Assert.assertEquals(menuRestaurantDto, menuRestaurantService.getMenuForId(1L));
	}

	@Test
	public void shouldNotReturnMenuForId() {
		when(menuRestaurantRepository.findById(anyLong())).thenReturn(Optional.empty());
		final MenuRestaurantDto menuRestaurantDto = new MenuRestaurantDto();
		Assert.assertEquals(menuRestaurantDto, menuRestaurantService.getMenuForId(1L));
	}

	@Test
	public void shouldSaveMenuItem() {
		final MenuRestaurant menuRestaurantBefore = new MenuRestaurant(null, "name1", new Restaurant(1L, "name1", "image1"), 1f, "image1");
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		final MenuRestaurant menuRestaurantAfter = new MenuRestaurant(1L, "name1", restaurant, 1f, "image1");
		when(menuRestaurantRepository.save(menuRestaurantBefore)).thenReturn(menuRestaurantAfter);
		when(restaurantRepository.findRestaurantByName("name1")).thenReturn(restaurant);
		final MenuRestaurantDto menuRestaurantDtoBefore = new MenuRestaurantDto(null, menuRestaurantBefore.getName(),
				menuRestaurantBefore.getRestaurant().getName(), menuRestaurantBefore.getPrice(), menuRestaurantBefore.getImage());
		MenuRestaurantDto menuRestaurantDtoAfter = menuRestaurantService.saveMenuItem(menuRestaurantDtoBefore);
		Assert.assertEquals(menuRestaurantDtoAfter.getId(), menuRestaurantAfter.getId());
	}

	@Test(expected = JpaSystemException.class)
	public void shouldThrowExceptionWhenSaving(){
		final MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name1", new Restaurant(1L, "name1", "image1"), 1f, "image1");
		when(menuRestaurantRepository.save(menuRestaurant)).thenThrow(new JpaSystemException(new RuntimeException("save error")));
		final Restaurant restaurant = new Restaurant(1L, "name1", "image1");
		when(restaurantRepository.findRestaurantByName(anyString())).thenReturn(restaurant);
		menuRestaurantService.saveMenuItem(new MenuRestaurantDto(
				menuRestaurant.getId(),menuRestaurant.getName(),menuRestaurant.getRestaurant().getName(),menuRestaurant.getPrice(),menuRestaurant.getImage()));
	}

	@Test
	public void shouldDeleteMenuItem(){
		final MenuRestaurant menuRestaurant = new MenuRestaurant(1L, "name1", new Restaurant(1L, "name1", "image1"), 1f, "image1");
		when(menuRestaurantRepository.findById(1L)).thenReturn(Optional.of(menuRestaurant));
		doNothing().when(menuRestaurantRepository).delete(menuRestaurant);
		menuRestaurantService.deleteMenuItem(menuRestaurant.getId());
		verify(menuRestaurantRepository).delete(menuRestaurant);
	}

	@Test(expected = JpaSystemException.class)
	public void shouldThrowExceptionWhenDeleteMenuItem(){
		when(menuRestaurantRepository.findById(anyLong())).thenReturn(Optional.empty());
		doThrow(new JpaSystemException(new RuntimeException("delete error"))).when(menuRestaurantRepository).delete(any());
		menuRestaurantService.deleteMenuItem(1L);
	}
}
