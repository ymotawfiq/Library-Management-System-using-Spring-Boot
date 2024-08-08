package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.time.Year;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(name = "title", nullable = false)
    private String Title;

    @Column(name = "author", nullable = false)
    private String Author;

    @Column(name = "isbn", nullable = false, unique = true)
    private String ISBN;

    @Column(name = "publication_year", nullable = false)
    private Year PublicationYear;

    @JsonManagedReference
	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	private Set<BorrowingRecord> BorrowingRecords = null;

    @JsonManagedReference
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private Set<BookImages> BookImages;

    public Book() {
    }

    public Book(String id, String title, String author, String iSBN, Year publicationYear) {
        Id = id;
        Title = title;
        Author = author;
        ISBN = iSBN;
        PublicationYear = publicationYear;
    }

    public Book(String title, String author, String iSBN, Year publicationYear) {
        Title = title;
        Author = author;
        ISBN = iSBN;
        PublicationYear = publicationYear;
    }

    public String getId() {
        return Id;
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
