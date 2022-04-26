package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.MenuRestaurant;
import com.mediumstory.auth0springboot.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRestaurantRepository extends JpaRepository<MenuRestaurant,Long> {
	List<MenuRestaurant> findAllByRestaurant(Restaurant restaurant);

	MenuRestaurant findMenuRestaurantByName(String name);
}
