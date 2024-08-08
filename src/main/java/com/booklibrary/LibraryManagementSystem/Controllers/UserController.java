package com.booklibrary.LibraryManagementSystem.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.ForgetPasswordDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.LoginDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.RegisterDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.ResetPasswordDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.TwoFactorLoginDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateFullNameDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateUserNameDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.EmailConfirmationToken;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Roles;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.RolesRepository;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRepository;
import com.booklibrary.LibraryManagementSystem.Services.AuthenticationService;
import com.booklibrary.LibraryManagementSystem.Services.EmailServiceImpl;
import com.booklibrary.LibraryManagementSystem.Services.LogOutTokensService;
import com.booklibrary.LibraryManagementSystem.Services.Mapping.IMappingToDtoService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/patron")
@RestController
public class UserController {

	private final AuthenticationService _authenticationService;
	private final EmailServiceImpl _emailServiceImpl;
	private final PatronRepository _userRepository;
	private final RolesRepository _rolesRepository;
	private final HttpServletRequest _servletRequest;
	private final LogOutTokensService _logOutTokensService;
	private final IMappingToDtoService _mappingToDtoService;
	public UserController(
		AuthenticationService authenticationService, 
		EmailServiceImpl emailServiceImpl,
		PatronRepository userRepository, 
		RolesRepository rolesRepository, 
		HttpServletRequest servletRequest,
		LogOutTokensService logOutTokensService,
		IMappingToDtoService mappingService
		) {
		_authenticationService = authenticationService;
		_emailServiceImpl = emailServiceImpl;
		_userRepository = userRepository;
		_rolesRepository = rolesRepository;
		_servletRequest = servletRequest;
		_logOutTokensService = logOutTokensService;
		_mappingToDtoService = mappingService;
	}

	@Async
	@PostMapping("/insert-roles")
	public ResponseEntity<ResponseModel<?>> InsertRoles(){
		try{
			if(_rolesRepository.findNumberOfRoles()>0){
				return ResponseEntity.ok(new ResponseModel<>(403, false, "Roles created successfully"));
			}
			List<Roles> roles = new ArrayList<>();
			roles.add(new Roles("user"));
			roles.add(new Roles("admin"));
			_rolesRepository.saveAll(roles);
			return ResponseEntity.ok(new ResponseModel<>(201, true, "Roles saved successfully"));
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
		}
	}
	
	@PostMapping("/register")
	@Async
	public ResponseEntity<ResponseModel<?>> Register(@RequestBody RegisterDto registerDto){
		try {
			ResponseModel<?> model = _authenticationService.Register(registerDto);
			return ResponseEntity.ok(model);
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
		}
	}
	
	@PostMapping("/login")
	@Async
	public ResponseEntity<ResponseModel<?>> Login(@RequestBody LoginDto loginDto) throws Exception{
		try {
			String token = _servletRequest.getHeader("Authorization");
			if(token != null){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			ResponseModel<?> model = _authenticationService.Login(loginDto);
			return ResponseEntity.ok(model);
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
		}
	}

	@GetMapping("/send-email-verification-link")
	@Async
	public ResponseEntity<ResponseModel<EmailConfirmationToken>> SendEmailVerificationLink(@RequestParam("email") String email) throws Exception{
		try {
			String token = _servletRequest.getHeader("Authorization");
			if(token != null){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			Optional<Patron> user = _userRepository.findByEmail(email);
			if(!user.isPresent()){
				return ResponseEntity.ok(new ResponseModel<EmailConfirmationToken>(200, true, "Check your inbox"));
			}
			if(user.get().getIsConfirmed()){
				return ResponseEntity.ok(new ResponseModel<EmailConfirmationToken>(403, false, "Email already confirmed"));
			}
			ResponseModel<EmailConfirmationToken> emailConfirmationToken = _authenticationService.generateEmailConfirmationToken(user.get());
			if(emailConfirmationToken.getData()==null){
				return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, "Failed to generate email comfirmation token")); 
			}
			_emailServiceImpl.sendConfirmationEmail(emailConfirmationToken.getData());
			return ResponseEntity.ok(new ResponseModel<EmailConfirmationToken>(200, true, "Check your inbox"));
		}
		catch (Exception e) {
			return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
		}
	}

	@GetMapping("/confirm-email")
	@Async
    public ResponseEntity<ResponseModel<?>> confirmEmail(
		@RequestParam("token") String emailToken,
		@RequestParam("userName") String userName) throws Exception {
        try{
			String token = _servletRequest.getHeader("Authorization");
			if(token != null){
				if(!_logOutTokensService.IsTokenKilled(token).isSuccess()){
					return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
				}
			}
			ResponseModel<?> response = _authenticationService.verifyUser(emailToken, userName);
            if(_authenticationService.verifyUser(token, userName).isSuccess()){
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(new ResponseModel<>(200, true, "Email verified successfully"));
            }
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
    }

	@Async
	@GetMapping("/me")
	public ResponseEntity<ResponseModel<?>> getUser(){
		try{
			String token = _servletRequest.getHeader("Authorization");
			if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
			Patron user = _authenticationService.findUser(authentication.getName());
			if(user!=null){
				return ResponseEntity.ok(new ResponseModel<>(200, true, "User found successfully", 
						_mappingToDtoService.PatronToPatronDto(user)));
			}
			return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized", authentication.getName()));			
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }	
	}

	@Async
	@PutMapping("/enable-two-factor")
	public ResponseEntity<ResponseModel<?>> EnableTwoFactorAuthentication() {
		try{
			String token = _servletRequest.getHeader("Authorization");
			if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
			Patron user = _authenticationService.findUser(authentication.getName());
			if(user!=null){
				ResponseModel<?> response = _authenticationService.EnableTwoFactorAuthentication(user);
				return ResponseEntity.ok(response);
			}
			return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized", authentication.getName()));			
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
	}

	@Async
	@PutMapping("/disable-two-factor")
	public ResponseEntity<ResponseModel<?>> DisableTwoFactorAuthentication() {
		try{
			String token = _servletRequest.getHeader("Authorization");
			if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
			Patron user = _authenticationService.findUser(authentication.getName());
			if(user!=null){
				ResponseModel<?> response = _authenticationService.DisableTwoFactorAuthentication(user);
				return ResponseEntity.ok(response);
			}
			return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized", authentication.getName()));			
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
	}

	@Async
	@PostMapping("/2FA-login")
	public ResponseEntity<ResponseModel<?>> TwoFactorLogin(@RequestBody TwoFactorLoginDto  twoFactorLogin) {
		try{

			String token = _servletRequest.getHeader("Authorization");
			if(token != null){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			
			ResponseModel<?> response = _authenticationService.LoginWithTwoFactor(twoFactorLogin);
			return ResponseEntity.ok(response);
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
	}

	@Async
	@PostMapping("/logout")
	public ResponseEntity<ResponseModel<?>> Logout(){
		try{
			String token = _servletRequest.getHeader("Authorization");
			if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			ResponseModel<?> response = _logOutTokensService.KillToken(token);
			return ResponseEntity.ok(response);
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
	}

	@Async
	@PutMapping("/update-username")
	public ResponseEntity<ResponseModel<?>> UpdateUserName(@RequestBody UpdateUserNameDto updateUserNameDto){
		try{
			String token = _servletRequest.getHeader("Authorization");
			if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			Patron user = _authenticationService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
			ResponseModel<?> response = _authenticationService.UpdateUserName(user, updateUserNameDto);
			return ResponseEntity.ok(response);
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
	}

	@Async
	@PutMapping("/update-fullname")
	public ResponseEntity<ResponseModel<?>> UpdateFullName(@RequestBody UpdateFullNameDto updateFullNameDto){
		try{
			String token = _servletRequest.getHeader("Authorization");
			if(token == null || _logOutTokensService.IsTokenKilled(token).isSuccess()){
				return ResponseEntity.ok(new ResponseModel<>(401, false, "Un Authorized"));
			}
			Patron user = _authenticationService.findUser(SecurityContextHolder.getContext().getAuthentication().getName());
			ResponseModel<?> response = _authenticationService.UpdateFullName(user, updateFullNameDto);
			return ResponseEntity.ok(response);
		}
		catch (Exception e){
            return ResponseEntity.internalServerError().body(
					new ResponseModel<>(500, false, e.getMessage()));
        }
	}

	@Async
	@PostMapping("/forget-password")
	public ResponseEntity<ResponseModel<?>> ForgetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto) throws Exception{
		ResponseModel<?> response = _authenticationService.ForgetPassword(forgetPasswordDto);
		return ResponseEntity.ok(response);
	}

	@Async
	@PutMapping("/reset-password")
	public ResponseEntity<ResponseModel<?>> ResetPassword(@RequestBody ResetPasswordDto resetPasswordDto){
		ResponseModel<?> response = _authenticationService.ResetPassword(resetPasswordDto);
		return ResponseEntity.ok(response);
	}
	
	
}
