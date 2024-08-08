package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;

public class ForgetPasswordDto {

    @NotBlank
    private String email;

    public ForgetPasswordDto() {
    }

    public ForgetPasswordDto(@NotBlank String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

}
