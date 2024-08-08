package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.ResetPasswordsTokens;

@Repository
public interface ResetPasswordsTokensRepository extends CrudRepository<ResetPasswordsTokens, String> {


    @Query(value = "SELECT * FROM reset_passwords_tokens WHERE code=?1", nativeQuery = true)
    Optional<ResetPasswordsTokens> findByCode(String code);

    @Query(value = "SELECT * FROM reset_passwords_tokens WHERE user_id=?1", nativeQuery = true)
    Optional<ResetPasswordsTokens> findByUserId(String userId);

}
