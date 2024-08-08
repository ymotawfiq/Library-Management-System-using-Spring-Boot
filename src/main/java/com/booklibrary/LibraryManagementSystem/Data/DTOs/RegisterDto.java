package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Email;

public class RegisterDto {

	@NotNull 
	@NotBlank (message = "Full Name Is Required")
	@NotEmpty 
	private String fullName;
	
	@NotNull 
	@NotBlank (message = "User Name Is Required")
	@NotEmpty 
	@Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", 
		message = "Invalid Email Format")
	private String userName;
	
	@NotNull 
	@NotBlank (message = "Email Is Required")
	@NotEmpty
	private String email;
	
	@NotBlank (message = "Password Is Required")
	private String password;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
	
	
	
}
