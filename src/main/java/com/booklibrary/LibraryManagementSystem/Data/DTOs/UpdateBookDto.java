package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import java.time.Year;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public class UpdateBookDto {

    @NotBlank
    private String id;

    @NotBlank
    private String title;
    
    @NotBlank
    private String author;
    
    @NotBlank
    private Year publicationYear;
    
    @NotBlank @Length(min = 10, max = 13)
    private String isbn;


    public UpdateBookDto(@NotBlank String id, @NotBlank String title, @NotBlank String author,
            @NotBlank Year publicationYear, @NotBlank @Length(min = 10, max = 13) String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
    }

    public UpdateBookDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Year getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Year publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
