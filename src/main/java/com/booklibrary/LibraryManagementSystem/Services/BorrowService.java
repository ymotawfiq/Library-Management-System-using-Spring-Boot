package com.booklibrary.LibraryManagementSystem.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.BorrowBookDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Book;
import com.booklibrary.LibraryManagementSystem.Data.Entities.BorrowingRecord;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.BookRepository;
import com.booklibrary.LibraryManagementSystem.Repository.BorrowRepository;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRolesRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowService {

    private final BorrowRepository _borrowRepository;
    private final BookRepository _bookRepository;
    private final PatronRolesRepository _patronRolesRepository;

    public BorrowService(
        BorrowRepository borrowRepository,
        BookRepository bookRepository,
        PatronRolesRepository patronRolesRepository
    )
    {
        _bookRepository = bookRepository;
        _borrowRepository = borrowRepository;
        _patronRolesRepository = patronRolesRepository;
    }

    @Async
    @Transactional
    public ResponseModel<?> BorrowBook(Patron patron, BorrowBookDto borrowBookDto){
        Optional<Book> book = _bookRepository.findById(borrowBookDto.getBookId());
        if(!book.isPresent()){
            return new ResponseModel<>(404, false, 
                String.format("Book with id (%s) not found", borrowBookDto.getBookId()));
        }
        Optional<BorrowingRecord> isBorrowed = _borrowRepository.findByPatronAndBookId(patron.getId(), borrowBookDto.getBookId());
        if(isBorrowed.isPresent())
        {
            if(LocalDateTime.now().compareTo(isBorrowed.get().getReturnDate())>0){
                _borrowRepository.delete(isBorrowed.get());
                BorrowingRecord borrowingRecord = new BorrowingRecord(patron, book.get(), LocalDateTime.now(), LocalDateTime.now().plusDays(7));
                _borrowRepository.save(borrowingRecord);
                return new ResponseModel<>(201, true, "Borrowed successfully, This book will be available for you from "+borrowingRecord.getBorrowingDate()+" To "+borrowingRecord.getReturnDate(), borrowingRecord);
            }
            return new ResponseModel<>(403, false, "You already borrowed this book from "+ isBorrowed.get().getBorrowingDate() + " to " +isBorrowed.get().getReturnDate());
        }
        _borrowRepository.save(new BorrowingRecord(patron, book.get(), LocalDateTime.now(), LocalDateTime.now().plusDays(7)));
        return new ResponseModel<>(201, true, "Borrowed successfully");
    }

    @Async
    @Transactional
    public ResponseModel<?> UnBorrowBook(Patron patron, String bookId){
        Optional<Book> book = _bookRepository.findById(bookId);
        if(!book.isPresent()){
            return new ResponseModel<>(404, false, 
                String.format("Book with id (%s) not found", bookId));
        }
        Optional<BorrowingRecord> isBorrowed = _borrowRepository.findByPatronAndBookId(patron.getId(), bookId);
        if(isBorrowed.isPresent())
        {
            _borrowRepository.delete(isBorrowed.get());
            return new ResponseModel<>(204, true, "Un Borrowed successfully, This book will be available for you from ");
        }
        return new ResponseModel<>(403, false, "You are not borrowing this book");
    }

    @Async
    @Transactional
    public ResponseModel<?> GetPatronBorrowedBooks(Patron patron){
        List<BorrowingRecord> borrowingRecords = _borrowRepository.getPatronBorrowingBooks(patron.getId());
        if(borrowingRecords == null || borrowingRecords.size() == 0){
            return new ResponseModel<>(204, true, "No borrowed books found");
        }
        return new ResponseModel<>(200, true, "Borrowed books found successfully", borrowingRecords);
    }

    @Async
    @Transactional
    public ResponseModel<?> GetBookPatrons(Patron patron, String bookId){
        if(!_patronRolesRepository.isPatronInRole(patron.getId(), "ADMIN").isPresent()){
            return new ResponseModel<>(401, false, "unauthorized");
        }
        List<BorrowingRecord> borrowingRecords = _borrowRepository.getBookBorrowingPatrons(bookId);
        if(borrowingRecords == null || borrowingRecords.size() == 0){
            return new ResponseModel<>(204, true, "No patrons found");
        }
        return new ResponseModel<>(200, true, "Patrons found successfully", borrowingRecords);
    }

}
