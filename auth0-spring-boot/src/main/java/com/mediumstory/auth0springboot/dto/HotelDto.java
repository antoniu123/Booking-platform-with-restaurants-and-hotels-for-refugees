package com.mediumstory.auth0springboot.dto;

import java.io.Serializable;
import java.util.Objects;

public class HotelDto implements Serializable {
	private Long id;
	private String name;
	private String zone;
	private Integer nr_rooms;
	private String image;

	public HotelDto(Long id, String name, String category, Integer nr_rooms, String image) {
		this.id = id;
		this.name = name;
		this.zone = category;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof HotelDto)) {
			return false;
		}
		HotelDto hotelDto = (HotelDto) o;
		return Objects.equals(id, hotelDto.id) && Objects.equals(name, hotelDto.name) && Objects.equals(zone, hotelDto.zone) && Objects.equals(nr_rooms, hotelDto.nr_rooms) && Objects.equals(image, hotelDto.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, zone, nr_rooms, image);
	}
}
