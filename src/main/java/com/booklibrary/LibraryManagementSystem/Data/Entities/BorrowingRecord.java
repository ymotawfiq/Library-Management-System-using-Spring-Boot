package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "borrowing_record", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"patron_id", "book_id"}))
public class BorrowingRecord {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @CreationTimestamp
    @Column(name = "borrowing_date", nullable = false)
    private LocalDateTime BorrowingDate;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime ReturnDate;

    public BorrowingRecord() {
    }

    public BorrowingRecord(Patron patron, Book book, LocalDateTime borrowingDate, LocalDateTime returnDate) {
        this.patron = patron;
        this.book = book;
        BorrowingDate = borrowingDate;
        ReturnDate = returnDate;
    }

    public String getId() {
        return Id;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowingDate() {
        return BorrowingDate;
    }

    public void setBorrowingDate(LocalDateTime borrowingDate) {
        BorrowingDate = borrowingDate;
    }

    public LocalDateTime getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        ReturnDate = returnDate;
    }

    

}
