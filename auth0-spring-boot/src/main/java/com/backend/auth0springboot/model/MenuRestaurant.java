package com.backend.auth0springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "MENU_RESTAURANT")
public class MenuRestaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RESTAURANT_ID")
	private Restaurant restaurant;

	@Column(name = "PRICE", nullable = false)
	private Float price;

	@Column(name = "IMAGE")
	private String image;

	public MenuRestaurant() {
	}

	public MenuRestaurant(Long id, String name, Restaurant restaurant, Float price, String image) {
		this.id = id;
		this.name = name;
		this.restaurant = restaurant;
		this.price = price;
		this.image = image;
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

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "MenuRestaurant{" +
				"id=" + id +
				", name='" + name + '\'' +
				", restaurant=" + restaurant +
				", price=" + price +
				", image='" + image + '\'' +
				'}';
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
		if (!(o instanceof MenuRestaurant)) {
			return false;
		}
		MenuRestaurant that = (MenuRestaurant) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(restaurant, that.restaurant) && Objects.equals(price, that.price) && Objects.equals(image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, restaurant, price, image);
	}
}
