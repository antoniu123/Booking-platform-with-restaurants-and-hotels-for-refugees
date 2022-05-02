package com.backend.auth0springboot.repository;

import com.backend.auth0springboot.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine,Long> {
}
