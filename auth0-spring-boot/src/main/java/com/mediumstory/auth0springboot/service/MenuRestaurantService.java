package com.mediumstory.auth0springboot.service;

import com.mediumstory.auth0springboot.dto.MenuRestaurantDto;
import com.mediumstory.auth0springboot.model.Hotel;
import com.mediumstory.auth0springboot.model.MenuRestaurant;
import com.mediumstory.auth0springboot.model.Restaurant;
import com.mediumstory.auth0springboot.repository.MenuRestaurantRepository;
import com.mediumstory.auth0springboot.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuRestaurantService {
	private final MenuRestaurantRepository menuRestaurantRepository;

	private final RestaurantRepository restaurantRepository;

	@Autowired
	public MenuRestaurantService(final MenuRestaurantRepository menuRestaurantRepository,
								 final RestaurantRepository restaurantRepository) {
		this.menuRestaurantRepository = menuRestaurantRepository;
		this.restaurantRepository = restaurantRepository;
	}

	public List<MenuRestaurantDto> getMenuForRestaurantId(Long restaurantId){
		Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
		return restaurant.map(value -> menuRestaurantRepository.findAllByRestaurant(value).stream()
				.map(m -> new MenuRestaurantDto(m.getId(),
						m.getRestaurant().getName(),
						m.getName(), m.getPrice(),
						m.getImage()))
						.collect(Collectors.toList()))
				.orElse(Collections.EMPTY_LIST);
	}

	public MenuRestaurantDto getMenuForId(Long id){
		return menuRestaurantRepository.findById(id)
						.map(m -> new MenuRestaurantDto(m.getId(),
								m.getRestaurant().getName(),
								m.getName(), m.getPrice(),
								m.getImage())).orElseGet(MenuRestaurantDto::new);
	}

	public List<MenuRestaurantDto> getAllMenuItemsFromAllRestaurants(){
		return menuRestaurantRepository.findAll().stream()
				.map(menuRestaurant ->	new MenuRestaurantDto(menuRestaurant.getId(),
						menuRestaurant.getRestaurant().getName(),
						menuRestaurant.getName(),menuRestaurant.getPrice(),
						menuRestaurant.getImage())).collect(Collectors.toList());
	}

	public MenuRestaurant getMenuItemById(Long id){
		return menuRestaurantRepository.findById(id).orElseGet(MenuRestaurant::new);
	}

	public MenuRestaurantDto saveMenuItem(MenuRestaurantDto menuRestaurantDto){
		MenuRestaurant menuRestaurant = new MenuRestaurant(menuRestaurantDto.getId(),menuRestaurantDto.getName(),
				  										   restaurantRepository.findRestaurantByName(menuRestaurantDto.getRestaurantName()),menuRestaurantDto.getPrice(),menuRestaurantDto.getImage());
		MenuRestaurant menuRestaurantNew = menuRestaurantRepository.save(menuRestaurant);
		return new MenuRestaurantDto(menuRestaurantNew.getId(),menuRestaurantNew.getRestaurant().getName(),
									 menuRestaurantNew.getName(),menuRestaurantNew.getPrice(),menuRestaurantNew.getImage());
	}

	public void deleteMenuItem(Long menuItemId){
		MenuRestaurant menuRestaurant = menuRestaurantRepository.findById(menuItemId).orElseGet(MenuRestaurant::new);
		menuRestaurantRepository.delete(menuRestaurant);
	}
}
