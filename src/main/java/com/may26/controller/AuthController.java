package com.may26.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.may26.dto.AuthRequest;
import com.may26.dto.AuthResponse;
import com.may26.dto.OtpRequest;
import com.may26.dto.ResetPasswordRequest;
import com.may26.dto.UpdateProfileDto;
import com.may26.dto.UserProfileDto;
import com.may26.entity.RefreshToken;
import com.may26.entity.User;
import com.may26.jwt.JwtUtil;
import com.may26.repository.RefreshTokenRepository;
import com.may26.repository.UserRepository;
import com.may26.service.UserService;
import com.may26.task.enums.Role;
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private  RefreshTokenRepository  refreshTokenRepository;
	
	@PostMapping("/register")
	public String register(@RequestBody AuthRequest request) {
		
		if(userRepository.findByEmail(request.getEmail()).isPresent()) {
			return "Email already exists";
		}
		User user = new User();

		user.setName(request.getName());
		user.setEmail(request.getEmail());

		user.setPassword(
		    userService.encryptPassword(request.getPassword())
		);

		// Default role for every new user
		user.setRole(Role.ROLE_USER);

		userRepository.save(user);
		
		return "User registred Successfully";
	}
	
	@PostMapping("/login")
	public String login(@RequestBody AuthRequest request) {
		System.out.println("LOGIN API HIT");
	    User user = userRepository.findByEmail(request.getEmail()).orElse(null);

	    if (user == null) {
	        return "User Not Found!";
	    }

	    boolean isPasswordCorrect =
	            userService.matchPassword(
	                    request.getPassword(),
	                    user.getPassword()
	            );

	    if (!isPasswordCorrect) {
	        return "Invalid Password!";
	    }

	    // Generate OTP
	    String otp = String.valueOf(
	            100000 + new java.util.Random().nextInt(900000)
	    );

	    // Save OTP
	    user.setOtp(otp);
	    user.setOtpExpiry(
	            java.time.LocalDateTime.now().plusMinutes(5)
	    );

	    userRepository.save(user);

	    // Send Email
	    userService.sendOtpEmail(user.getEmail(), otp);

	    // DON'T Generate JWT here
	    return "OTP Sent Successfully";
	}
	
	@PostMapping("/verify-otp")
	public AuthResponse verifyOtp(@RequestBody OtpRequest request) {
	    return userService.verifyOtp(request);
	}
	
	@PostMapping("/forgot-password")
	public String forgotPassword(
	        @RequestBody AuthRequest request
	) {

	    return userService.forgotPassword(
	            request.getEmail()
	    );

	}
	
	@PostMapping("/reset-password")
	public String resetPassword(
	        @RequestBody ResetPasswordRequest request
	) {

	    return userService.resetPassword(request);

	}
	
	
	
	@GetMapping("/profile")
	public UserProfileDto getProfile(Authentication authentication) {

	    return userService.getProfile(authentication.getName());
	}
	
	@PutMapping("/profile")
	public UserProfileDto updateProfile(
	        Authentication authentication,
	        @RequestBody UpdateProfileDto dto
	) {

	    return userService.updateProfile(
	            authentication.getName(),
	            dto
	    );
	}
	
	@PostMapping("/upload-profile-image")
	public String uploadProfileImage(
	        Authentication authentication,
	        @RequestParam("image") MultipartFile image
	) throws Exception {

	    return userService.uploadProfileImage(
	            authentication.getName(),
	            image
	    );
	}
	
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestParam String token) {

	    RefreshToken rt = refreshTokenRepository.findByToken(token)
	            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

	    if (rt.getExpiryDate().isBefore(LocalDateTime.now())) {
	        return ResponseEntity.status(401).body("Refresh token expired");
	    }

	    // 🔥 IMPORTANT: use real role from DB (not hardcoded)
	    User user = userRepository.findByEmail(rt.getEmail())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    String newAccessToken =
	            jwtUtil.generateToken(
	                    user.getEmail(),
	                    user.getRole().name()
	            );

	    return ResponseEntity.ok(newAccessToken);
	}
	@GetMapping("/role")
	public String getRole(Authentication authentication) {
	    return userService.getUserRole(authentication.getName());
	}
}
