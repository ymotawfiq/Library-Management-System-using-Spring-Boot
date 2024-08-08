package com.booklibrary.LibraryManagementSystem.Data.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Builder;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Builder
@Entity
@Table(name = "patrons")
public class Patron implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false)
	private String Id;
	
	@Column(name = "full_name", nullable = false)
	private String FullName;
	
	@Column(name = "user_name", nullable = false, unique = true, 
			updatable = true)
	private String UserName;

	@Column(name = "normalized_user_name", nullable = false, unique = true, 
			updatable = true)
	private String NormalizedUserName;
	
	@Column(name = "email", nullable = false, unique = true, 
			updatable = true)
	private String Email;

	@Column(name = "normalized_email", nullable = false, unique = true, 
			updatable = true)
	private String NormalizedEmail;
	
	@Column(nullable = false, name = "password")
	private String Password;

	@Column(nullable = false)
	private boolean isConfirmed = false;

	@Column(nullable = false, name = "is_two_factor_enabled")
	private boolean isTwoFactorEnabled = false;


	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private Date CreatedAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Date UpdatedAt;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "patron", fetch = FetchType.EAGER)
	private Set<PatronRoles> UserRoles;

	@JsonManagedReference
	@OneToMany(mappedBy = "patron", fetch = FetchType.EAGER)
	private Set<BorrowingRecord> BorrowingRecords;

	
	public Patron(String fullName, String userName, String email, String password) {
		this.FullName = fullName;
		this.UserName = userName;
		this.Email = email;
		this.Password = password;
		this.isConfirmed = false;
		this.NormalizedEmail = this.Email.toUpperCase();
		this.NormalizedUserName = this.UserName.toUpperCase();
	}
	
	

	public Patron() {}


	public void setUserRoles(Set<PatronRoles> userRoles) {
		UserRoles = userRoles;
	}



	public Set<PatronRoles> getUserRoles() {
		return this.UserRoles;
	}

	public boolean getIsConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public String getId() {
		return Id;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}


	public Date getUpdatedAt() {
		return UpdatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		UpdatedAt = updatedAt;
	}

	public boolean isTwoFactorEnabled() {
		return isTwoFactorEnabled;
	}

	public void setTwoFactorEnabled(boolean isTwoFactorEnabled) {
		this.isTwoFactorEnabled = isTwoFactorEnabled;
	}


	public String getNormalizedUserName() {
		return NormalizedUserName;
	}

	public void setNormalizedUserName(String normalizedUserName) {
		NormalizedUserName = normalizedUserName;
	}

	public String getNormalizedEmail() {
		return NormalizedEmail;
	}

	public void setNormalizedEmail(String normalizedEmail) {
		NormalizedEmail = normalizedEmail;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<PatronRoles> roles = this.UserRoles;
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (PatronRoles userRole : roles) {
			authorities.add(new SimpleGrantedAuthority(userRole.getRole().getRole()));
		}
		return List.of();
	}

	@Override
	public String getUsername() {
		return this.UserName;
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public Set<BorrowingRecord> getBorrowingRecords() {
		return BorrowingRecords;
	}



	public void setBorrowingRecords(Set<BorrowingRecord> borrowingRecords) {
		BorrowingRecords = borrowingRecords;
	}



	public void setId(String id) {
		Id = id;
	}



	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}
	
	
}
