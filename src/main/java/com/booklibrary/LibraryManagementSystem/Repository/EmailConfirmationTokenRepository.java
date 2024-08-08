package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.EmailConfirmationToken;

@Repository
public interface EmailConfirmationTokenRepository extends CrudRepository<EmailConfirmationToken, String> {

    @Query(value = "SELECT * FROM email_confirmation_token WHERE token = ?1", 
        nativeQuery = true)
    Optional<EmailConfirmationToken> findByToken(String token);

    @Query(value = "SELECT * FROM email_confirmation_token WHERE user_id = ?1", 
        nativeQuery = true)
        Optional<EmailConfirmationToken> findByUserId(String userId);

    @Query(value = "SELECT * FROM email_confirmation_token WHERE user_id = ?1 AND token=?2", 
        nativeQuery = true)
    Optional<EmailConfirmationToken> findByUserIdAndToken(String userId, String token);

    @Query(value = "DELETE FROM email_confirmation_token WHERE user_id = ?1", 
        nativeQuery = true)
    EmailConfirmationToken deleteByUserId(Integer userId);

    

}
