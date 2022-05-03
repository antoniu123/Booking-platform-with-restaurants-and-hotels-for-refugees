package com.backend.auth0springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "HELP_POINT")
public class HelpPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "ADDRESS", nullable = false)
	private String address;

	public HelpPoint() {
	}

	public HelpPoint(Long id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
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
		if (!(o instanceof HelpPoint)) {
			return false;
		}
		HelpPoint helpPoint = (HelpPoint) o;
		return Objects.equals(id, helpPoint.id) && Objects.equals(name, helpPoint.name) && Objects.equals(address, helpPoint.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address);
	}
}
