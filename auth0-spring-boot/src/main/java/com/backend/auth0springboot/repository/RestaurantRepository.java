package com.backend.auth0springboot.repository;

import com.backend.auth0springboot.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Restaurant findRestaurantByName(String name);
}
