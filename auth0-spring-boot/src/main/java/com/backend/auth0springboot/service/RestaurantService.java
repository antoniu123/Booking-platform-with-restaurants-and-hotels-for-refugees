package com.backend.auth0springboot.service;

import com.backend.auth0springboot.dto.RestaurantDto;
import com.backend.auth0springboot.model.Restaurant;
import com.backend.auth0springboot.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;

	@Autowired
	public RestaurantService(final RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	public List<RestaurantDto> getAllRestaurants(){
		return restaurantRepository.findAll().stream().map(restaurant -> new RestaurantDto(restaurant.getId(),restaurant.getName(),restaurant.getImage())).collect(Collectors.toList());
	}

	public RestaurantDto getRestaurantById(Long id){
		return restaurantRepository.findById(id)
				.map(rest->new RestaurantDto(rest.getId(),rest.getName(),rest.getImage()))
				.orElseGet(RestaurantDto::new);
	}

	public RestaurantDto saveRestaurant(RestaurantDto restaurantDto){
		Restaurant restaurant = new Restaurant(restaurantDto.getId(),restaurantDto.getName(),restaurantDto.getImage());
		Restaurant restaurantNew = restaurantRepository.save(restaurant);
		return new RestaurantDto(restaurantNew.getId(),restaurantDto.getName(),restaurantDto.getImage());
	}

	public void deleteRestaurant(Long restaurantId){
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseGet(Restaurant::new);
		restaurantRepository.delete(restaurant);
	}

}
