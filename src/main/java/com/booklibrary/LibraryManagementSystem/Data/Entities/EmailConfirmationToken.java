package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.ReadOnlyProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_confirmation_token")
public class EmailConfirmationToken {


    @Id
    @Column(name = "id", nullable = false)
    private String Id;

    @Column(unique = true, nullable = false, name = "token")
    private String Token;

    @CreationTimestamp
    @ReadOnlyProperty
    @Column(name = "created_at")
    private LocalDateTime CreatedAt;


    @Column(name = "expires_at")
    private LocalDateTime ExpiresAt;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Patron user;


    public EmailConfirmationToken(String token, Patron user) {
        Token = token;
        this.user = user;
        CreatedAt = LocalDateTime.now();
        ExpiresAt = LocalDateTime.now().plusMinutes(10);
        Id = user.getId();
    }


    public EmailConfirmationToken() {
    }

    public String getId() {
        return Id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }


    public Patron getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return CreatedAt;
    }

    public LocalDateTime getExpiresAt() {
        return ExpiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        ExpiresAt = expiresAt;
    }

    

    

}
