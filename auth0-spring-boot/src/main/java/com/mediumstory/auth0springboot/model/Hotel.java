package com.mediumstory.auth0springboot.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOTEL")

public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "ZONE", nullable = false)
	private String zone;

	@Column(name = "NR_ROOMS", nullable = false)
	private Integer nr_rooms;

	@Column(name = "IMAGE")
	private String image;

	public Hotel() {
	}

	public Hotel(Long id, String name, String zone, Integer nr_rooms, String image) {
		this.id = id;
		this.name = name;
		this.zone = zone;
		this.nr_rooms = nr_rooms;
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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public Integer getNr_rooms() {
		return nr_rooms;
	}

	public void setNr_rooms(Integer nr_rooms) {
		this.nr_rooms = nr_rooms;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}

