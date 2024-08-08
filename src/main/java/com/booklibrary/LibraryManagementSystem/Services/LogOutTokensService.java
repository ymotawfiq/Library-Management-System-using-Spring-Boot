package com.booklibrary.LibraryManagementSystem.Services;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.booklibrary.LibraryManagementSystem.Data.Entities.LogOutTokens;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.LogOutTokenRepository;

import jakarta.transaction.Transactional;

@Service
public class LogOutTokensService {


    private final LogOutTokenRepository _logOutTokenRepository;
    public LogOutTokensService(LogOutTokenRepository logOutTokenRepository){
        _logOutTokenRepository = logOutTokenRepository;
    }

    @Async
    @Transactional
    public ResponseModel<?> KillToken(String token){
        Optional<LogOutTokens> existToken = _logOutTokenRepository.findById(token);
        if(existToken.isPresent()){
            return new ResponseModel<>(403, false, "Token already killed");
        }
        _logOutTokenRepository.save(new LogOutTokens(token));
        return new ResponseModel<>(201, true, "Logged out successfully");
    }

    @Async
    public ResponseModel<?> IsTokenKilled(String token){
        Optional<LogOutTokens> existToken = _logOutTokenRepository.findById(token);
        if(existToken.isPresent()){
            return new ResponseModel<>(200, true, "Token already killed");
        }
        return new ResponseModel<>(204, false, "Token not killed");
    }

}
