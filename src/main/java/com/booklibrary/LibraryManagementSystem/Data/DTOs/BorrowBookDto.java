package com.booklibrary.LibraryManagementSystem.Data.DTOs;

import jakarta.validation.constraints.NotBlank;

public class BorrowBookDto {

    @NotBlank
    private String bookId;

    public BorrowBookDto() {
    }

    public BorrowBookDto(@NotBlank String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    
}
