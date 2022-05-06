package com.backend.auth0springboot.dto;

import java.util.List;
import java.util.Objects;

public class OrderDto {
	private Long id;
	private String restaurantName;
	private String status;
	private Float price;

	private List<OrderLineDto> orderLines;
	private String userId;

	public OrderDto(Long id,
					String restaurantName,
					String status,
					Float price,
					List<OrderLineDto> orderLines,
					String userId) {
		this.id = id;
		this.restaurantName = restaurantName;
		this.status = status;
		this.orderLines = orderLines;
		this.price = price;
		this.userId = userId;
	}

	public OrderDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}


	public List<OrderLineDto> getOrderLines() {
		return orderLines;
	}

	public void setOrderLineDtoList(List<OrderLineDto> orderLines) {
		this.orderLines = orderLines;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof OrderDto)) {
			return false;
		}
		OrderDto orderDto = (OrderDto) o;
		return Objects.equals(id, orderDto.id) && Objects.equals(restaurantName, orderDto.restaurantName) && Objects.equals(status, orderDto.status) && Objects.equals(price, orderDto.price) && Objects.equals(orderLines, orderDto.orderLines) && Objects.equals(userId, orderDto.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, restaurantName, status, price, orderLines, userId);
	}
}
