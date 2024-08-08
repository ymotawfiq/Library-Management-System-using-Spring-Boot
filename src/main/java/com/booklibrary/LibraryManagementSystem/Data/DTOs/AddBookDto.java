package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import java.time.Year;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;

public class AddBookDto {

    @NotBlank
    private String title;
    
    @NotBlank
    private String author;
    
    @NotBlank
    private Year publicationYear;
    
    @NotBlank @Length(min = 10, max = 13)
    private String isbn;

    @Nullable
    private List<MultipartFile> images;

    public AddBookDto() {
    }

    
    public AddBookDto(@NotBlank String title, @NotBlank String author, @NotBlank Year publicationYear,
            @NotBlank @Length(min = 10, max = 13) String isbn, List<MultipartFile> images) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.images = images;
    }


    public AddBookDto(@NotBlank String title, @NotBlank String author, @NotBlank Year publicationYear,
            @NotBlank @Length(min = 10, max = 13) String isbn) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
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


    public List<MultipartFile> getImages() {
        return images;
    }


    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

}
