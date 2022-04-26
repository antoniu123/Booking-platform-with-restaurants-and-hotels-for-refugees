package com.mediumstory.auth0springboot.repository;

import com.mediumstory.auth0springboot.model.Order;
import com.mediumstory.auth0springboot.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine,Long> {
}
