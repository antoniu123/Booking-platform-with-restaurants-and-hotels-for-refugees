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
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "RESERVATION")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HOTEL_ID")
	private Hotel hotel;

	@Column(name = "DATE_IN", nullable = false)
	private Date dateIn;

	@Column(name = "DATE_OUT", nullable = false)
	private Date dateOut;

	@Column(name = "VALID", nullable = false)
	private Integer valid;

	@Column(name = "USER_ID")
	private String userId;

	public Reservation() {
	}

	public Reservation(Long id, Hotel hotel, Date dateIn, Date dateOut, Integer valid, String userId) {
		this.id = id;
		this.hotel = hotel;
		this.dateIn = dateIn;
		this.dateOut = dateOut;
		this.valid = valid;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Date getDateIn() {
		return dateIn;
	}

	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Reservation)) {
			return false;
		}
		Reservation that = (Reservation) o;
		return Objects.equals(id, that.id) && Objects.equals(hotel, that.hotel) && Objects.equals(dateIn, that.dateIn) && Objects.equals(dateOut, that.dateOut) && Objects.equals(valid, that.valid) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, hotel, dateIn, dateOut, valid, userId);
	}
}
