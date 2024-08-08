package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;

public class UpdateFullNameDto {

    @NotBlank
    private String fullName;

    public UpdateFullNameDto() {
    }

    public UpdateFullNameDto(@NotBlank String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    
}
