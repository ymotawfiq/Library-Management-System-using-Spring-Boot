package com.booklibrary.LibraryManagementSystem.Controllers;

import org.springframework.security.core.Authentication;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.AddBookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookAuthorDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookIsbnDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookTitleDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRepository;
import com.booklibrary.LibraryManagementSystem.Services.BookService;
import com.booklibrary.LibraryManagementSystem.Services.LogOutTokensService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final PatronRepository _patronRepository;
    private final BookService _bookService;
    private final HttpServletRequest _servletRequest;
    private final LogOutTokensService _logOutTokensService;

    public BookController(
        PatronRepository patronRepository,
        BookService bookService,
        HttpServletRequest servletRequest,
        LogOutTokensService logOutTokensService
    )
    {
        _bookService = bookService;
        _patronRepository = patronRepository;
        _servletRequest = servletRequest;
        _logOutTokensService = logOutTokensService;
    }

    @Async
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseModel<?>> AddBook(@ModelAttribute AddBookDto addBookDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.AddBook(patron.get(), addBookDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @PutMapping("/update-book")
    public ResponseEntity<ResponseModel<?>> UpdateBook(@RequestBody UpdateBookDto updateBookDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.UpdateBook(patron.get(), updateBookDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @PutMapping("/update-book-title")
    public ResponseEntity<ResponseModel<?>> UpdateBookTitle(@RequestBody UpdateBookTitleDto updateBookDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.UpdateBookTitle(patron.get(), updateBookDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @PutMapping("/update-book-author")
    public ResponseEntity<ResponseModel<?>> UpdateBookAuthor(@RequestBody UpdateBookAuthorDto updateBookDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.UpdateBookAuthor(patron.get(), updateBookDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @PutMapping("/update-book-isbn")
    public ResponseEntity<ResponseModel<?>> UpdateBookIsbn(@RequestBody UpdateBookIsbnDto updateBookDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.UpdateBookIsbn(patron.get(), updateBookDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @GetMapping("/find-by-id/{bookId}")
    public ResponseEntity<ResponseModel<?>> FindBookById(@PathVariable(name = "bookId") String bookId){
        try{
            ResponseModel<?> response = _bookService.FindById(bookId);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @GetMapping("/find-by-isbn/{isbn}")
    public ResponseEntity<ResponseModel<?>> FindBookByIsbn(@PathVariable(name = "isbn") String isbn){
        try{
            ResponseModel<?> response = _bookService.FindByIsbn(isbn);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @DeleteMapping("/delete-by-isbn/{isbn}")
    public ResponseEntity<ResponseModel<?>> DeleteBookByIsbn(@PathVariable(name = "isbn") String isbn){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.DeleteByIsbn(isbn);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @DeleteMapping("/delete-by-id/{bookId}")
    public ResponseEntity<ResponseModel<?>> DeleteBookById(@PathVariable(name = "bookId") String bookId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.DeleteById(bookId);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @PutMapping("/{bookId}/{imgId}")
    public ResponseEntity<ResponseModel<?>> MakeBookImageDefault(
        @PathVariable(name = "bookId") String bookId, 
        @PathVariable(name = "imageId") String imageId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _bookService.MakeBookImageDefault(patron.get(), bookId, imageId);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }


    @Async
    @GetMapping
    public ResponseEntity<ResponseModel<?>> FindAllBooks(){
        try{
            ResponseModel<?> response = _bookService.FindAll();
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }


}
