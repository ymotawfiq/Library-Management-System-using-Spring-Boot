package com.booklibrary.LibraryManagementSystem.Services;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.ForgetPasswordDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.LoginDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.RegisterDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.ResetPasswordDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.TwoFactorLoginDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateFullNameDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateUserNameDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.EmailConfirmationToken;
import com.booklibrary.LibraryManagementSystem.Data.Entities.ResetPasswordsTokens;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Roles;
import com.booklibrary.LibraryManagementSystem.Data.Entities.TwoFactorCode;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Data.Entities.PatronRoles;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.EmailConfirmationTokenRepository;
import com.booklibrary.LibraryManagementSystem.Repository.ResetPasswordsTokensRepository;
import com.booklibrary.LibraryManagementSystem.Repository.RolesRepository;
import com.booklibrary.LibraryManagementSystem.Repository.TwoFactorCodeRepository;
import com.booklibrary.LibraryManagementSystem.Services.Mapping.IMappingToDtoService;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRepository;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRolesRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

	private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(20);
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
	
	private final PatronRepository _userRepository;
	private final PasswordEncoder _passwordEncoder;
	private final RolesRepository _rolesRepository;
	private final PatronRolesRepository _userRolesRepository;
	private final EmailConfirmationTokenRepository _emailConfirmationToken;
	private final EmailServiceImpl _emailServiceImpl;
	private final TwoFactorCodeRepository _twoFactorCodeRepository;
	private final ResetPasswordsTokensRepository _resetPasswordsTokensRepository;
	private final IMappingToDtoService _mappingToDtoService;
	
	private final JwtService _jwtService;
	
	public AuthenticationService(
			PatronRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			RolesRepository rolesRepository,
			PatronRolesRepository userRolesRepository,
			EmailConfirmationTokenRepository emailConfirmationToken,
			EmailServiceImpl emailServiceImpl,
			TwoFactorCodeRepository twoFactorCodeRepository,
			ResetPasswordsTokensRepository resetPasswordsTokensRepository,
			IMappingToDtoService mappingService
			) {
		_passwordEncoder = passwordEncoder;
		_userRepository = userRepository;
		_jwtService = jwtService;
		_rolesRepository = rolesRepository;
		_userRolesRepository = userRolesRepository;
		_emailConfirmationToken = emailConfirmationToken;
		_emailServiceImpl = emailServiceImpl;
		_twoFactorCodeRepository = twoFactorCodeRepository;
		_resetPasswordsTokensRepository = resetPasswordsTokensRepository;
		_mappingToDtoService = mappingService;
	}


	@Transactional
	@Async
	public ResponseModel<?> Register(RegisterDto registerDto) throws Exception {
		Optional<Patron> userByName = _userRepository.findByUserName(registerDto.getUserName());
		
		if(userByName.isPresent()) {
			return new ResponseModel<Patron>(403, false, "User Name already exists");
		}
		Optional<Patron> userByEmail = _userRepository.findByEmail(registerDto.getEmail());
		if(userByEmail.isPresent()) {
			return new ResponseModel<Patron>(403, false, "Email already exists");
		}
		Patron user = new Patron(registerDto.getFullName(), registerDto.getUserName(),
				registerDto.getEmail(), 
				_passwordEncoder.encode(registerDto.getPassword()));
		_userRepository.save(user);
		AssignUserToUserRole(user);
		EmailConfirmationToken e = generateEmailConfirmationToken(user).getData();
		_emailServiceImpl.sendConfirmationEmail(e);
		return new ResponseModel<>(201, true, "Registered successfully check your inbox to confirm your email", 
				_mappingToDtoService.PatronToPatronDto(user));		
	}
	
	@Async
	@Transactional
	public ResponseModel<?> Login(LoginDto loginDto) throws Exception{
		Patron user = findUser(loginDto.getUserNameOrEmail());
		if(user==null || 
				!_passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
					return new ResponseModel<>(500, false, "Invalid user name or password");
				}
		else if(!user.getIsConfirmed()){
			return new ResponseModel<>(403, false, "Failed to generate token please confirm your email");
		}
		if(!user.isTwoFactorEnabled()){
			String token = _jwtService.generateToken(user);
			return new ResponseModel<>(200, true, "Token generated successfully", token);
		}
		Optional<TwoFactorCode> code = _twoFactorCodeRepository.findByUserId(user.getId());
		if(code.isPresent()){
			return new ResponseModel<>(200, true, "Check your inbox");
		}
		TwoFactorCode newCode = new TwoFactorCode(user);
		_twoFactorCodeRepository.save(newCode);
		_emailServiceImpl.sendTwoFactorCode(newCode);
		return new ResponseModel<>(200, true, "Check your inbox to get two factor code");
	}

	@Async
	@Transactional
	public ResponseModel<?> LoginWithTwoFactor(TwoFactorLoginDto twoFactorLogin) throws Exception{
		Patron user = findUser(twoFactorLogin.getEmail());
		if(user==null){
			return new ResponseModel<>(500, false, "Invalid email or code");
		}
		Optional<TwoFactorCode> twoFactorCode = _twoFactorCodeRepository.findByUserId(user.getId());
		if(!twoFactorCode.isPresent()){
			return new ResponseModel<>(500, false, "Invalid email or code");
		}
		else if(twoFactorCode.get().getCode() != twoFactorLogin.getCode()){
			return new ResponseModel<>(500, false, "Invalid email or code");
		}
		else if(LocalDateTime.now().compareTo(twoFactorCode.get().getExpiresAt())>0){
			return new ResponseModel<>(500, false, "Invalid email or code");
		}
		String token = _jwtService.generateToken(user);
		_twoFactorCodeRepository.delete(twoFactorCode.get());
		return new ResponseModel<>(200, true, "Token generated successfully", token);
	}
	
	@Async
	@Transactional
	public ResponseModel<?> verifyUser(String token, String userName) throws Exception{
		Optional<Patron> user = _userRepository.findByUserName(userName);
		if(!user.isPresent()){
			return new ResponseModel<>(500, false, "Invalid token");
		}
		Optional<EmailConfirmationToken> e = _emailConfirmationToken.findByUserIdAndToken(user.get().getId(), token);
		if(!e.isPresent()){
			return new ResponseModel<>(500, false, "Invalid token");
		}
		LocalDateTime c = LocalDateTime.now();
		if(c.compareTo(e.get().getExpiresAt())>0){
			return new ResponseModel<>(500, false, "Expires token");
		}
		user.get().setConfirmed(true);
		_userRepository.save(user.get());
		_emailConfirmationToken.delete(e.get());
		return new ResponseModel<>(200, true, "Email confirmed successfully", true);
	}

	@Async
	@Transactional
	public ResponseModel<EmailConfirmationToken> generateEmailConfirmationToken(Patron user){
		Optional<EmailConfirmationToken> o = _emailConfirmationToken.findByUserId(user.getId());
		if(o.isPresent()){
			_emailConfirmationToken.delete(o.get());
		}
		String tokenValue = new String(Base64.getEncoder().encode(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
		EmailConfirmationToken e = new EmailConfirmationToken(tokenValue, user);
		_emailConfirmationToken.save(e);
		return new ResponseModel<>(201, true, "Token generated successfully", e);
	}

	@Async
	public Patron findUser(String userNameOrEmail) {
		Optional<Patron> userByEmail = _userRepository.findByEmail(userNameOrEmail);
		if(userByEmail.isPresent())return userByEmail.get();
		Optional<Patron> userByName = _userRepository.findByUserName(userNameOrEmail);
		if(userByName.isPresent())return userByName.get();
		return null;
	}

	@Async
	@Transactional
	public ResponseModel<?> EnableTwoFactorAuthentication(Patron user){
		if(user.isTwoFactorEnabled()){
			return new ResponseModel<>(403, false, "Two factor already enabled");
		}
		user.setTwoFactorEnabled(true);
		_userRepository.save(user);
		return new ResponseModel<>(200, true, "Two factor enabled successfully");
	}


	@Async
	@Transactional
	public ResponseModel<?> DisableTwoFactorAuthentication(Patron user){
		if(!user.isTwoFactorEnabled()){
			return new ResponseModel<>(403, false, "Two factor already disabled");
		}
		user.setTwoFactorEnabled(false);
		_userRepository.save(user);
		return new ResponseModel<>(200, true, "Two factor disabled successfully");
	}


	@Async
	@Transactional
	public ResponseModel<?> UpdateUserName(Patron user, UpdateUserNameDto updateUserNameDto){
		Optional<Patron> existUser = _userRepository.findByUserName(updateUserNameDto.getUserName());
		if(existUser.isPresent()){
			return new ResponseModel<>(403, false, "User Name already exists");
		}
		user.setUserName(updateUserNameDto.getUserName());
		user.setNormalizedUserName(updateUserNameDto.getUserName().toUpperCase());
		_userRepository.save(user);
		return new ResponseModel<>(200, true, "User Name updated successfully",
				_mappingToDtoService.PatronToPatronDto(user));
	}

	@Async
	@Transactional
	public ResponseModel<?> UpdateFullName(Patron user, UpdateFullNameDto updateFullNameDto){
		user.setFullName(updateFullNameDto.getFullName());
		_userRepository.save(user);
		return new ResponseModel<>(200, true, "Full name updated successfully",
				_mappingToDtoService.PatronToPatronDto(user));
	}

	@Async
	@Transactional
	public ResponseModel<?> ForgetPassword(ForgetPasswordDto forgetPasswordDto) throws Exception{
		Optional<Patron> user = _userRepository.findByEmail(forgetPasswordDto.getEmail());
		if(!user.isPresent()){
			return new ResponseModel<>(200, true, "Check your inbox to get reset password code");
		}
		Optional<ResetPasswordsTokens> code = _resetPasswordsTokensRepository.findByUserId(user.get().getId());
		if(code.isPresent()){
			_resetPasswordsTokensRepository.delete(code.get());
		}
		ResetPasswordsTokens newCode = new ResetPasswordsTokens(user.get());
		_resetPasswordsTokensRepository.save(newCode);
		_emailServiceImpl.sendResetPasswordCode(newCode);
		return new ResponseModel<>(200, true, "Check your inbox to get reset password code");
	}

	@Async
	@Transactional
	public ResponseModel<?> ResetPassword(ResetPasswordDto resetPasswordDto){
		Optional<Patron> user = _userRepository.findByEmail(resetPasswordDto.getEmail());
		if(!user.isPresent()){
			return new ResponseModel<>(403, false, "Invalid email or code");
		}
		Optional<ResetPasswordsTokens> code = _resetPasswordsTokensRepository.findByCode(resetPasswordDto.getCode());
		if(!code.isPresent()){
			return new ResponseModel<>(403, false, "Invalid email or code");
		}
		else if(!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())){
			return new ResponseModel<>(400, false, "Password and confirmed password doesn't match");
		}
		else if(LocalDateTime.now().compareTo(code.get().getExpiresAt())>0){
			return new ResponseModel<>(403, false, "Invalid email code");
		}
		user.get().setPassword(_passwordEncoder.encode(resetPasswordDto.getNewPassword()));
		_userRepository.save(user.get());
		_resetPasswordsTokensRepository.delete(code.get());
		return new ResponseModel<>(200, true, "Password reset successfully");
	}	

	@Async
	@Transactional
	private ResponseModel<?> AssignUserToUserRole(Patron user){
		Optional<Roles> userRole = _rolesRepository.findByRoleName("user".toUpperCase());
		if(!userRole.isPresent()){
			_rolesRepository.save(new Roles("user"));
		}
		Optional<PatronRoles> isInRole = _userRolesRepository
			.findByUserIdAndRoleId(user.getId(), userRole.get().getId());
		if(isInRole.isPresent()){
			return new ResponseModel<>(403, false, "User already in role", false);	
		}
		_userRolesRepository.save(new PatronRoles(user, userRole.get()));		
		return new ResponseModel<>(201, true, "Roles assigned successfully", true);
	}
	
}
