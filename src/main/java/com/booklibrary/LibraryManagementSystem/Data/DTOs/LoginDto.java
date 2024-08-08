package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;

public class LoginDto {

	@NotBlank (message = "User Name Or Email Is Required")
	private String userNameOrEmail;
	
	@NotBlank (message = "Password Is Required")
	private String password;

	public String getUserNameOrEmail() {
		return userNameOrEmail;
	}

	public void setUserNameOrEmail(String userNameOrEmail) {
		this.userNameOrEmail = userNameOrEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
