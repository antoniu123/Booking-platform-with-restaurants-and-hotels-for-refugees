package com.backend.auth0springboot.dto;

import java.io.Serializable;
import java.util.Objects;

public class RestaurantDto implements Serializable {
	private Long id;
	private String name;
	private String image;

	public RestaurantDto(Long id, String name, String image) {
		this.id = id;
		this.name = name;
		this.image = image;
	}

	public RestaurantDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		if (!(o instanceof RestaurantDto)) {
			return false;
		}
		RestaurantDto that = (RestaurantDto) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, image);
	}
}
