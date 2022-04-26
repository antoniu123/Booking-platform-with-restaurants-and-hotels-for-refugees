package com.mediumstory.auth0springboot.dto;

public class OrderLineDto {
	private Long id;
	private String menuRestaurantName;
	private Integer quantity;
	private Float price;

	public OrderLineDto(Long id, String menuRestaurantName, Integer quantity, Float price) {
		this.id = id;
		this.menuRestaurantName = menuRestaurantName;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuRestaurantName() {
		return menuRestaurantName;
	}

	public void setMenuRestaurantName(String menuRestaurantName) {
		this.menuRestaurantName = menuRestaurantName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}
}
