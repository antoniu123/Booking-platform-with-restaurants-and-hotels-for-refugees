package com.backend.auth0springboot.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ReservationDto implements Serializable {
	private Long id;
	private String hotelName;
	private Date dateIn;
	private Date dateOut;
	private Integer valid;
	private String userId;

	public ReservationDto(Long id, String hotelName, Date dateIn, Date dateOut, Integer valid, String userId) {
		this.id = id;
		this.hotelName = hotelName;
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

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
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
		if (!(o instanceof ReservationDto)) {
			return false;
		}
		ReservationDto that = (ReservationDto) o;
		return Objects.equals(id, that.id) && Objects.equals(hotelName, that.hotelName) && Objects.equals(dateIn, that.dateIn) && Objects.equals(dateOut, that.dateOut) && Objects.equals(valid, that.valid) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, hotelName, dateIn, dateOut, valid, userId);
	}
}
