package com.mediumstory.auth0springboot.service;

import com.mediumstory.auth0springboot.dto.MenuRestaurantDto;
import com.mediumstory.auth0springboot.model.MenuRestaurant;
import com.mediumstory.auth0springboot.model.Restaurant;
import com.mediumstory.auth0springboot.repository.MenuRestaurantRepository;
import com.mediumstory.auth0springboot.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

	public List<MenuRestaurantDto> getAllMenuItems(Long id){
		return menuRestaurantRepository.findAllByRestaurant(restaurantRepository.getById(id)).stream()
				.map(menuRestaurant ->	new MenuRestaurantDto(menuRestaurant.getId(),
						menuRestaurant.getRestaurant().getName(),
						menuRestaurant.getName(),menuRestaurant.getPrice(),
						menuRestaurant.getImage())).collect(Collectors.toList());
	}

	public MenuRestaurant getMenuItemById(Long id){
		return menuRestaurantRepository.findById(id).orElseGet(MenuRestaurant::new);
	}
}
