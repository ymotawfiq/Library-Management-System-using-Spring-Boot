package com.booklibrary.LibraryManagementSystem.Data.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patron_roles")
@IdClass(PatronRoles.class)
public class PatronRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles role;

    public PatronRoles(){}
    
    public PatronRoles(Patron patron, Roles role) {
        this.patron = patron;
        this.role = role;
    }

    public Patron getUser() {
        return patron;
    }

    public Roles getRole() {
        return role;
    }

    public String getId() {
        return Id;
    }

}
