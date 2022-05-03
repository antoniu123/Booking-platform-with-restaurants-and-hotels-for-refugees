package com.backend.auth0springboot.dto;

import java.io.Serializable;
import java.util.Objects;

public class HelpPointDto implements Serializable {
	private long id;
	private String name;
	private String address;

	public HelpPointDto() {
	}

	public HelpPointDto(long id, String name, String address) {
		this.id = id;
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof HelpPointDto)) {
			return false;
		}
		HelpPointDto that = (HelpPointDto) o;
		return id == that.id && Objects.equals(name, that.name) && Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address);
	}

	@Override
	public String toString() {
		return "HelpPointDto{" +
				"id=" + id +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}
