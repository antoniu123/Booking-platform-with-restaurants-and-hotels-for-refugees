package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}