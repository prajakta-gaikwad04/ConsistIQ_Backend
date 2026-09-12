package com.may26.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.may26.entity.User;
import com.may26.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
    	String path = request.getServletPath();

    	if (path.equals("/auth/register")
    	        || path.equals("/auth/login")
    	        || path.equals("/auth/verify-otp")
    	        || path.equals("/auth/forgot-password")
    	        || path.equals("/auth/reset-password")
    	        || path.startsWith("/swagger-ui")
    	        || path.startsWith("/v3/api-docs")
    	        || path.startsWith("/uploads")) {

    	    filterChain.doFilter(request, response);
    	    return;
    	
    	}
    	System.out.println("Request URI: " + request.getRequestURI());

    

        // ✅ Skip if already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
    	System.out.println("Authorization: " + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                // ✅ Validate token
                boolean isValid = jwtUtil.validateToken(token);

                if (isValid) {

                    // ✅ Extract email from token
                    String email = jwtUtil.extractEmail(token);

                    // Optional debug
                    System.out.println("JWT VALID USER: " + email);

                    // ✅ Fetch user from DB
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    // ✅ Convert role to Spring Security format
                    List<SimpleGrantedAuthority> authorities =
                    		List.of(new SimpleGrantedAuthority(user.getRole().name()));
                    // ✅ Create authentication object
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    authorities
                            );

                    // ✅ Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("User Authenticated: " + email + " | Role: " + user.getRole());
                }

            } catch (Exception e) {
                System.out.println("JWT Error: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}