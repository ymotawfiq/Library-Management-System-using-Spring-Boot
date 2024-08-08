package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.time.LocalDateTime;
import java.util.Random;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "two_factor_code")
public class TwoFactorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(name = "code", nullable = false, unique = true)
    private int Code;

    @CreationTimestamp
    private LocalDateTime CreatedAt;
    
    private LocalDateTime ExpiresAt;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Patron user;

    public TwoFactorCode(Patron user) {
        Random rnd = new Random();
        this.CreatedAt = LocalDateTime.now();
        this.ExpiresAt = LocalDateTime.now().plusMinutes(10);
        this.Code = rnd.nextInt(999999);
        this.user = user;
    }

    public TwoFactorCode(){}

    public String getId() {
        return Id;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return ExpiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        ExpiresAt = expiresAt;
    }

    public Patron getUser() {
        return user;
    }

    public void setUser(Patron user) {
        this.user = user;
    }

    


}
