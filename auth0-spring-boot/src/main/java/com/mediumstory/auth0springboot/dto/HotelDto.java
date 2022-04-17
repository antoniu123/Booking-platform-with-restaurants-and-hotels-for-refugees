package com.mediumstory.auth0springboot.dto;

import java.io.Serializable;

public class HotelDto implements Serializable {
	private long id;
	private String name;
	private String zone;
	private Integer nr_rooms;
	private String image;

	public HotelDto(long id, String name, String category, Integer nr_rooms, String image) {
		this.id = id;
		this.name = name;
		this.zone = category;
		this.nr_rooms = nr_rooms;
		this.image = image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
