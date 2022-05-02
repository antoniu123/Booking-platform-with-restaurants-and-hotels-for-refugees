package com.backend.auth0springboot.dto;

public class ResponseDto {

	private String message;

	public ResponseDto(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}