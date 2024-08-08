package com.booklibrary.LibraryManagementSystem.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.booklibrary.LibraryManagementSystem.Data.Entities.BorrowingRecord;

@Repository
public interface BorrowingRecordRepository extends CrudRepository<BorrowingRecord, String> {

    @Query(value = "SELECT * FROM borrowing_record WHERE patron_id=?1", 
        nativeQuery = true)
    List<BorrowingRecord> findPatronBorrowingRecords(String patronId);

    @Query(value = "SELECT * FROM borrowing_record WHERE patron_id=?1 AND book_id=?2", 
        nativeQuery = true)
    Optional<BorrowingRecord> isPatronBorrowingBook(String patronId, String bookId);

    @Query(value = "SELECT * FROM borrowing_record WHERE book_id=?1", 
        nativeQuery = true)
    List<BorrowingRecord> findPatronsBorrowingBook(String bookId);

}
