package com.backend.auth0springboot.dto;

import java.io.Serializable;
import java.util.Objects;

public class MenuRestaurantDto implements Serializable {
	private Long id;
	private String restaurantName;
	private String name;
	private Float price;
	private String image;

	public MenuRestaurantDto(Long id, String restaurantName, String name, Float price, String image) {
		this.id = id;
		this.restaurantName = restaurantName;
		this.name = name;
		this.price = price;
		this.image = image;
	}

	public MenuRestaurantDto() {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof MenuRestaurantDto)) {
			return false;
		}
		MenuRestaurantDto that = (MenuRestaurantDto) o;
		return Objects.equals(id, that.id) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, restaurantName, name, price, image);
	}
}
