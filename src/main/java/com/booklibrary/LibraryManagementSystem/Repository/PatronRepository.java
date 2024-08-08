package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;


@Repository
public interface PatronRepository extends CrudRepository<Patron, String> {
	
	@Query(value = "SELECT * FROM patrons WHERE email=?1 LIMIT 1", nativeQuery = true)
	Optional<Patron> findByEmail(String email);

	@Query(value = "SELECT id FROM patrons WHERE email=?1 LIMIT 1", nativeQuery = true)
	String findId(String email);
	
	@Query(value = "SELECT * FROM patrons WHERE user_name=?1 LIMIT 1", nativeQuery = true)
	Optional<Patron> findByUserName(String username);
	
}
