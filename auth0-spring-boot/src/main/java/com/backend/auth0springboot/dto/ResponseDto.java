package com.backend.auth0springboot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public class ResponseDto implements Serializable {

	private String message;

	@JsonCreator
	public ResponseDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}