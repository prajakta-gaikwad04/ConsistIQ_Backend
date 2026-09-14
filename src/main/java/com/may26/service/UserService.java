package com.may26.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.may26.task.enums.Role;

@Service
public class UserService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
		@Autowired
		private  RefreshTokenRepository  refreshTokenRepository;
	private BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
	
	public String encryptPassword(String password) {
		return encoder.encode(password);
	}
	
	public boolean matchPassword(String rawPassword,String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
		
	}
	
	public String getUserRole(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return user.getRole().toString();
	}
	public boolean isAdmin(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return user.getRole() == Role.ROLE_ADMIN;
	}
	public List<User> getAllUsers() {
	    return userRepository.findByDeletedFalse();
	}

	public List<User> getDeletedUsers() {
	    return userRepository.findByDeletedTrue();
	}

	public void deleteUser(Long id) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setDeleted(true);
	    user.setDeletedAt(LocalDateTime.now());

	    userRepository.save(user);
	}

	public void restoreUser(Long id) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setDeleted(false);
	    user.setDeletedAt(null);

	    userRepository.save(user);
	}
	
	public Role getRoleByEmail(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return user.getRole();
	}
	public void sendOtpEmail(String email, String otp) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setTo(email);

	    message.setSubject("ConsistIQ Login Verification");

	    message.setText(
	            "Hello,\n\n" +
	            "Your login OTP is: " + otp +
	            "\n\nThis OTP is valid for 5 minutes." +
	            "\n\nDo not share this OTP with anyone." +
	            "\n\nRegards,\nConsistIQ Team"
	    );

	    mailSender.send(message);
	}
	
	public AuthResponse verifyOtp(OtpRequest request) {

	    User user = userRepository
	            .findByEmail(request.getEmail())
	            .orElse(null);

	    if (user == null) {
	        throw new RuntimeException("User Not Found!");
	    }

	    if (user.getOtp() == null) {
	        throw new RuntimeException("Please login first.");
	    }

	    if (!user.getOtp().equals(request.getOtp())) {
	        System.out.println("Entered OTP : " + request.getOtp());
	        System.out.println("Stored OTP  : " + user.getOtp());
	        throw new RuntimeException("Invalid OTP!");
	    }

	    if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException("OTP Expired!");
	    }

	    // ✅ clear OTP after success
	    user.setOtp(null);
	    user.setOtpExpiry(null);

	    // ✅ update last login
	    user.setLastLogin(LocalDateTime.now());

	    userRepository.save(user);

	    // 🔐 Generate Access Token
	    String accessToken =
	            jwtUtil.generateToken(
	                    user.getEmail(),
	                    user.getRole().name()
	            );

	    // 🔁 Generate Refresh Token
	    String refreshToken =
	            generateRefreshToken(user.getEmail());

	    // 📦 Return structured response
	    AuthResponse response = new AuthResponse();
	    response.setAccessToken(accessToken);
	    response.setRefreshToken(refreshToken);
	    response.setRole(user.getRole().name());

	    return response;
	}
	public String forgotPassword(String email) {

	    User user = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    String otp =
	            String.valueOf(
	                    100000 +
	                    new Random().nextInt(900000));

	    user.setOtp(otp);

	    user.setOtpExpiry(
	            LocalDateTime.now().plusMinutes(5));

	    userRepository.save(user);

	    sendOtpEmail(email, otp);

	    return "OTP Sent Successfully";
	}
	
	public String resetPassword(ResetPasswordRequest request) {

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    if (user.getOtp() == null || user.getOtpExpiry() == null) {
	        return "Please request OTP first.";
	    }

	    if (!user.getOtp().equals(request.getOtp())) {
	        return "Invalid OTP!";
	    }

	    if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
	        return "OTP Expired!";
	    }

	    user.setPassword(encoder.encode(request.getNewPassword()));

	    user.setOtp(null);
	    user.setOtpExpiry(null);

	    userRepository.save(user);

	    return "Password Reset Successfully";
	}
	
	
	
	public UserProfileDto getProfile(String email) {

	    User user = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    UserProfileDto dto = new UserProfileDto();

	    dto.setName(user.getName());
	    dto.setEmail(user.getEmail());

	    dto.setPhone(user.getPhone());
	    dto.setCity(user.getCity());
	    dto.setCountry(user.getCountry());
	    dto.setBio(user.getBio());

	    dto.setProfileImage(user.getProfileImage());
	    dto.setAvatarStyle(user.getAvatarStyle());

	    dto.setCreatedAt(user.getCreatedAt());
	    dto.setLastLogin(user.getLastLogin());

	    dto.setEmailVerified(user.isEmailVerified());
	    dto.setTwoFactorEnabled(user.isTwoFactorEnabled());

	    return dto;
	}
	
	
	
	public UserProfileDto updateProfile(
	        String currentEmail,
	        UpdateProfileDto dto
	) {

	    User user = userRepository
	            .findByEmail(currentEmail)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    // Email Update
	    if (!user.getEmail().equals(dto.getEmail())) {

	        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
	            throw new RuntimeException("Email already exists");
	        }

	        user.setEmail(dto.getEmail());
	    }

	    // Basic Details
	    user.setName(dto.getName());
	    user.setPhone(dto.getPhone());
	    user.setCity(dto.getCity());
	    user.setCountry(dto.getCountry());
	    user.setBio(dto.getBio());

	    // Avatar Style
	    user.setAvatarStyle(dto.getAvatarStyle());

	    // Custom Profile Image
	    user.setProfileImage(dto.getProfileImage());

	    userRepository.save(user);

	    UserProfileDto profile = new UserProfileDto();

	    profile.setName(user.getName());
	    profile.setEmail(user.getEmail());

	    profile.setPhone(user.getPhone());
	    profile.setCity(user.getCity());
	    profile.setCountry(user.getCountry());
	    profile.setBio(user.getBio());

	    profile.setAvatarStyle(user.getAvatarStyle());
	    profile.setProfileImage(user.getProfileImage());

	    profile.setCreatedAt(user.getCreatedAt());
	    profile.setLastLogin(user.getLastLogin());

	    profile.setEmailVerified(user.isEmailVerified());
	    profile.setTwoFactorEnabled(user.isTwoFactorEnabled());

	    return profile;
	}
	
	public String uploadProfileImage(
	        String email,
	        MultipartFile image
	) throws Exception {

	    User user = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    String fileName =
	            UUID.randomUUID() + "_" + image.getOriginalFilename();

	    String uploadDir =
	            System.getProperty("user.dir")
	            + "/uploads/";

	    File folder = new File(uploadDir);

	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    image.transferTo(
	            new File(uploadDir + fileName)
	    );

	    String imageUrl =
	            "http://localhost:8081/uploads/" + fileName;

	    user.setProfileImage(imageUrl);

	    userRepository.save(user);

	    return imageUrl;
	}
	
	public void makeAdmin(Long id) {

	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setRole(Role.ROLE_ADMIN);

	    userRepository.save(user);
	}
	
	
	public User getUserById(Long id) {
	    return userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User updateRole(Long id, Role role) {
	    User user = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setRole(role);
	    return userRepository.save(user);
	}

	
	
	public String generateRefreshToken(String email) {

	    String token = UUID.randomUUID().toString();

	    RefreshToken rt = new RefreshToken();
	    rt.setToken(token);
	    rt.setEmail(email);
	    rt.setExpiryDate(LocalDateTime.now().plusDays(7));

	    refreshTokenRepository.save(rt);

	    return token;
	}
	@Async
	public void sendLoginOtpEmailAsync(String email, String otp) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setTo(email);
	    message.setSubject("ConsistIQ Login Verification");

	    message.setText(
	            "Hello,\n\n" +
	            "Your login OTP is: " + otp +
	            "\n\nThis OTP is valid for 5 minutes." +
	            "\n\nDo not share this OTP with anyone." +
	            "\n\nRegards,\nConsistIQ Team"
	    );

	    mailSender.send(message);
	}
	
}
