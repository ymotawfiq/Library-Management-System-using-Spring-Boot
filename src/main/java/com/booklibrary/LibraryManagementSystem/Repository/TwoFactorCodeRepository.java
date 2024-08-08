package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.TwoFactorCode;

import jakarta.transaction.Transactional;

@Repository
public interface TwoFactorCodeRepository extends CrudRepository<TwoFactorCode, String> {

    @Query(value = "SELECT * FROM two_factor_code WHERE user_id=?1", nativeQuery = true)
    Optional<TwoFactorCode> findCodeByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM two_factor_code WHERE user_id=?1", nativeQuery = true)
    void deleteOldCode(String userId);

}
