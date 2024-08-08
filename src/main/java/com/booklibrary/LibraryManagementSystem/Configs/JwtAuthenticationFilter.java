package com.booklibrary.LibraryManagementSystem.Configs;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.booklibrary.LibraryManagementSystem.Services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver _handlerExceptionResolver;
	private final JwtService _jwtService;
	private final UserDetailsService _userDetailsService;
	
	public JwtAuthenticationFilter(
			HandlerExceptionResolver handlerExceptionResolver,
			JwtService jwtService, 
			UserDetailsService userDetailsService) {
		_handlerExceptionResolver = handlerExceptionResolver;
		_jwtService = jwtService;
		_userDetailsService = userDetailsService;
	}
	@Override
	protected void doFilterInternal(
			@NotNull HttpServletRequest request,
			@NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain)
					throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			final String jwt = authHeader.substring(7);
			final String userName = _jwtService.extractUserName(jwt);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(userName!=null && authentication==null) {
				UserDetails userDetails = _userDetailsService.loadUserByUsername(userName);
				if(_jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities()
							);
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			filterChain.doFilter(request, response);
		}
		catch (Exception e) {
            _handlerExceptionResolver.resolveException(request, response, null, e);
        }
	}

	
}
