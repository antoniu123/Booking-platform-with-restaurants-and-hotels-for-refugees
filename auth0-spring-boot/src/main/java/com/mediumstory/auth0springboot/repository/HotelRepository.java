package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
	Hotel findHotelByName(String name);
}
