package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TwoFactorLoginDto {

    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private int code;

    public TwoFactorLoginDto(@NotBlank @Email String email, @NotBlank int code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    

}
