package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name =  "reset_passwords_tokens")
public class ResetPasswordsTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "id", unique = true)
    private String Id;

    @Column(unique = true, name = "code", nullable = false)
    private String Code;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Patron user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime CreatedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime ExpiresAt;

    public ResetPasswordsTokens() {
    }    

    public ResetPasswordsTokens(Patron user) {
        this.Code = UUID.randomUUID().toString();
        this.user = user;
        this.CreatedAt = LocalDateTime.now();
        this.ExpiresAt = LocalDateTime.now().plusMinutes(10);
    }

    public String getId() {
        return Id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Patron getUser() {
        return user;
    }

    public void setUser(Patron user) {
        this.user = user;
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
