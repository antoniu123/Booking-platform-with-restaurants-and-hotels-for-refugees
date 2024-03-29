package com.backend.auth0springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "RESTAURANT")
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "IMAGE", nullable = false)
	private String image;

	public Restaurant() {

	}

	public Restaurant(Long id, String name, String image) {
		this.id = id;
		this.name = name;
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
		if (!(o instanceof Restaurant)) {
			return false;
		}
		Restaurant that = (Restaurant) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, image);
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + id +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				'}';
	}
}
