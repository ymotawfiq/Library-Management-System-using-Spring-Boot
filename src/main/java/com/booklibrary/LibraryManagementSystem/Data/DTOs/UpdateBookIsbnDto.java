package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public class UpdateBookIsbnDto {

    @NotBlank
    private String id;

    @NotBlank @Length(min = 10, max = 13)
    private String isbn;

    public UpdateBookIsbnDto() {
    }

    public UpdateBookIsbnDto(@NotBlank String id, @NotBlank String isbn) {
        this.id = id;
        this.isbn = isbn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }



    

}
