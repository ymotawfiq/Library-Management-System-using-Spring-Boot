package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.BookImages;

import jakarta.transaction.Transactional;

@Repository
public interface BookImagesRepository extends JpaRepository<BookImages, String> {

    @Query(value = "SELECT * FROM book_images WHERE book_id=?1", nativeQuery = true)
    List<BookImages> findBookImages(String bookId);

    @Query(value = "SELECT * FROM book_images WHERE book_id=?1 AND id=?2 LIMIT 1", nativeQuery = true)
    Optional<BookImages> findByBookAndImgId(String bookId, String imgId);

    @Query(value = "SELECT * FROM book_images WHERE book_id=?1 AND is_default=1 LIMIT 1", nativeQuery = true)
    Optional<BookImages> findBookDefaultImage(String bookId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM book_images WHERE book_id=?1", nativeQuery = true)
    void deleteBookImages(String bookId);

}
