package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_images")
public class BookImages {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(name = "image_path", nullable = false, unique = true)
    private String ImagePath;

    @Column(name = "is_default")
    private boolean IsDefault; 

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @JsonBackReference
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date CreatedAt;

    public BookImages() {
    }

    public BookImages(String imagePath, Book book) {
        ImagePath = imagePath;
        this.book = book;
        this.IsDefault = false;
    }

    public String getId() {
        return Id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public boolean isIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean isDefault) {
        IsDefault = isDefault;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    

}
