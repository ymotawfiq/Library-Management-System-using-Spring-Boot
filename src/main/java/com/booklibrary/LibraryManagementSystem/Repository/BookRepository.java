package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query(value = "SELECT * FROM books WHERE isbn=?1", 
        nativeQuery = true)
    Optional<Book> findByISBN(String isbn);

    @Query(value = "SELECT * FROM books", nativeQuery = true)
    List<Book> findAllBooks();

}
