package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}
