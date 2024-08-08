package com.booklibrary.LibraryManagementSystem.Controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.BorrowBookDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRepository;
import com.booklibrary.LibraryManagementSystem.Services.BorrowService;
import com.booklibrary.LibraryManagementSystem.Services.LogOutTokensService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final PatronRepository _patronRepository;
    private final BorrowService _borrowService;
    private final HttpServletRequest _servletRequest;
    private final LogOutTokensService _logOutTokensService;

    public BorrowController(
        PatronRepository patronRepository,
        BorrowService borrowService,
        HttpServletRequest servletRequest,
        LogOutTokensService logOutTokensService
    )
    {
        _servletRequest = servletRequest;
        _borrowService = borrowService;
        _patronRepository = patronRepository;
        _logOutTokensService = logOutTokensService;
    }

    @Async
    @PostMapping
    public ResponseEntity<ResponseModel<?>> BorrowBook(@RequestBody BorrowBookDto borrowBookDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token==null || _logOutTokensService.IsTokenKilled(token).isSuccess())
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent())
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            ResponseModel<?> response = _borrowService.BorrowBook(patron.get(), borrowBookDto);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @GetMapping
    public ResponseEntity<ResponseModel<?>> GetPatronBorrowedBooks(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token==null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _borrowService.GetPatronBorrowedBooks(patron.get());
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @GetMapping("/{bookId}")
    public ResponseEntity<ResponseModel<?>> GetBookPatrons(@PathVariable String bookId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null){
                String token = _servletRequest.getHeader("Authorization");
                if(_logOutTokensService.IsTokenKilled(token).isSuccess()){
                    return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
                }
                Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
                if(!patron.isPresent()){
                    return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
                }
                ResponseModel<?> response = _borrowService.GetBookPatrons(patron.get(), bookId);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

    @Async
    @DeleteMapping("/{bookId}")
    public ResponseEntity<ResponseModel<?>> UnBorrowBook(@PathVariable String bookId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = _servletRequest.getHeader("Authorization");
            if(token==null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
                return ResponseEntity.ok(new ResponseModel<>(401, false, "unauthorized"));
            }
            Optional<Patron> patron = _patronRepository.findByUserName(authentication.getName());
            if(!patron.isPresent()){
                return ResponseEntity.ok(new ResponseModel<>(404, false, "User not found"));
            }
            ResponseModel<?> response = _borrowService.UnBorrowBook(patron.get(), bookId);
            return ResponseEntity.ok(response);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError()
                .body(new ResponseModel<>(500, false, e.getMessage()));
        }
    }

}
