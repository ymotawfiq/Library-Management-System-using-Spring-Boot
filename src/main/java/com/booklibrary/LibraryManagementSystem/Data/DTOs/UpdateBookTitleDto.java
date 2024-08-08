package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;

public class UpdateBookTitleDto {
    
    @NotBlank
    private String id;

    @NotBlank
    private String title;

    public UpdateBookTitleDto() {
    }

    public UpdateBookTitleDto(@NotBlank String id, @NotBlank String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
