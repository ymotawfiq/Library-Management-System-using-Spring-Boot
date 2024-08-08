package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotNull;

public class UpdateUserNameDto {

    @NotNull
    private String userName;

    public UpdateUserNameDto() {
    }

    public UpdateUserNameDto(@NotNull String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
