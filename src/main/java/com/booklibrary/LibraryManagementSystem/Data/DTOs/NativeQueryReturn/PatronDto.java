package com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatronDto {

	@JsonProperty("id")
	private String Id;
	
	@JsonProperty("fullName")
	private String FullName;
	
	@JsonProperty("email")
	private String Email;
	
	@JsonProperty("userName")
	private String UserName;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}
	
	
	
}
