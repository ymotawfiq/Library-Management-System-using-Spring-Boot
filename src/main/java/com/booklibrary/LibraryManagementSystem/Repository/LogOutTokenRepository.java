package com.booklibrary.LibraryManagementSystem.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.LogOutTokens;

@Repository
public interface LogOutTokenRepository extends CrudRepository<LogOutTokens, String> {

}
