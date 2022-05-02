package com.backend.auth0springboot.repository;

import com.backend.auth0springboot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
