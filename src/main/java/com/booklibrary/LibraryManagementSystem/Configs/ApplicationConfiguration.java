package com.booklibrary.LibraryManagementSystem.Configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.booklibrary.LibraryManagementSystem.Repository.PatronRepository;



@Configuration
@EnableWebSecurity
public class ApplicationConfiguration {
	private final PatronRepository _userRepository;
	public ApplicationConfiguration(PatronRepository userRepository) {
		_userRepository = userRepository;
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		return username->_userRepository.findByUserName(username)
				.orElseThrow(()->new UsernameNotFoundException("User not found"));
	}
	
	@Bean 
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(userDetailsService());
		dao.setPasswordEncoder(passwordEncoder());
		return dao;
	}


	
}
