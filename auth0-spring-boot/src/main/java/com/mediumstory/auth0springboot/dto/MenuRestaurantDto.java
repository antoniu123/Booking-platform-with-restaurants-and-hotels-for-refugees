package com.mediumstory.auth0springboot.dto;

import java.io.Serializable;

public class MenuRestaurantDto implements Serializable {
	private long id;
	private String restaurantName;
	private String name;
	private Float price;
	private String image;

	public MenuRestaurantDto(long id, String restaurantName, String name, Float price, String image) {
		this.id = id;
		this.restaurantName = restaurantName;
		this.name = name;
		this.price = price;
		this.image = image;
	}

	public MenuRestaurantDto() {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
