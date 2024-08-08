package com.booklibrary.LibraryManagementSystem.Data.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "logout_tokens")
public class LogOutTokens {


    @Id
    @Column(name = "token", nullable = false, unique = true)
    private String Token;

    public LogOutTokens() {
    }

    public LogOutTokens(String token) {
        Token = token;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    

}
