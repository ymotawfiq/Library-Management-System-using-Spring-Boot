package com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn;

import java.time.Year;
import java.util.Set;

import com.booklibrary.LibraryManagementSystem.Data.Entities.BookImages;
import com.fasterxml.jackson.annotation.JsonProperty;


public class BookDto {

	@JsonProperty("id")
    private String Id;

	@JsonProperty("title")
    private String Title;

	@JsonProperty("author")
    private String Author;

	@JsonProperty("isbn")
    private String ISBN;

	@JsonProperty("publicationYear")
    private Year PublicationYear;
	
	@JsonProperty("bookImages")
	private Set<BookImages> BookImages;


    public BookDto() {
        super();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public Year getPublicationYear() {
        return PublicationYear;
    }

    public void setPublicationYear(Year publicationYear) {
        PublicationYear = publicationYear;
    }

	public Set<BookImages> getBookImages() {
		return BookImages;
	}

	public void setBookImages(Set<BookImages> bookImages) {
		BookImages = bookImages;
	}

    

}
