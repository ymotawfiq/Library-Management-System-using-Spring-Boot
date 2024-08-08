package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;

public class UpdateBookAuthorDto {

    @NotBlank
    private String id;

    @NotBlank
    private String author;

    public UpdateBookAuthorDto() {
    }

    public UpdateBookAuthorDto(@NotBlank String id, @NotBlank String author) {
        this.id = id;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    

}
