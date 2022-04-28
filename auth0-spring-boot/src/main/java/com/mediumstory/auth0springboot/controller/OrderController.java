package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.OrderDto;
import com.mediumstory.auth0springboot.dto.OrderLineDto;
import com.mediumstory.auth0springboot.dto.ReservationDto;
import com.mediumstory.auth0springboot.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDto> saveOrder(@RequestBody OrderDto orderDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrUpdateOrder(orderDto));
	}

	@GetMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDto>> getNewOrders() throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllNewOrders());
	}

	@GetMapping(value = "/restaurant/{restaurantId}/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDto>> getNewOrdersForRestaurantId(@PathVariable Long restaurantId) throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllNewOrdersForRestaurantId(restaurantId));
	}

	@GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(orderId));
	}

	@GetMapping(value = "/completed", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDto>> getCompletedOrders() throws SQLException {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllCompletedOrders());
	}

	@PostMapping(value = "/{orderId}/send", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> sendOrder(@PathVariable Long orderId) throws SQLException {
		orderService.sendOrder(orderId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
