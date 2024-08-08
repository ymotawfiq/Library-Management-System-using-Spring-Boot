package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(nullable = false, unique = true)
    private String Role;

    @Column(nullable = false, unique = true)
    private String NormalizedRoleName;

    @CreationTimestamp
    private Date CreatedAt;

    @UpdateTimestamp
    private Date UpdatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "role")
    private Set<PatronRoles> UserRoles;


    public Roles(String role) {
        this.Role = role;
        this.NormalizedRoleName = role.toUpperCase();
    }

    public Roles(){}

    public String getId() {
        return Id;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getNormalizedRoleName() {
        return NormalizedRoleName;
    }

    public void setNormalizedRoleName(String normalizedRoleName) {
        NormalizedRoleName = normalizedRoleName;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }


    public Set<PatronRoles> getUserRoles() {
        return UserRoles;
    }


    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }

    


}
