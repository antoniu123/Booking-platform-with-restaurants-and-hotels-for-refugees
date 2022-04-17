package com.mediumstory.auth0springboot.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class HelpPointDto implements Serializable {
	private long id;
	private String name;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private String address;

	public HelpPointDto() {
	}

	public HelpPointDto(long id, String name, BigDecimal longitude, BigDecimal latitude, String address) {
		this.id = id;
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
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

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "HelpPointDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				", longitude=" + longitude +
				", latitude=" + latitude +
				", address='" + address + '\'' +
				'}';
	}
}
