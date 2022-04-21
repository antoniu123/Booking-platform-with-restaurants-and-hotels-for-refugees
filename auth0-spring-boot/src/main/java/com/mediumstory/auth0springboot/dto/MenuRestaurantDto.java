package com.mediumstory.auth0springboot.dto;

import java.io.Serializable;

public class MenuRestaurantDto implements Serializable {
	private long id;
	private String restaurantName;
	private String orderName;
	private Integer price;
	private String image;

	public MenuRestaurantDto(long id, String restaurantName, String orderName, Integer price, String image) {
		this.id = id;
		this.restaurantName = restaurantName;
		this.orderName = orderName;
		this.price = price;
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
