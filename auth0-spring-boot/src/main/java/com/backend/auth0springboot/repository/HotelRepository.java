package com.backend.auth0springboot.repository;

import com.backend.auth0springboot.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
	Hotel findHotelByName(String name);
}
