package com.reddit.clone.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.clone.dto.AuthenticationResponse;
import com.reddit.clone.dto.LoginRequest;
import com.reddit.clone.dto.RefreshTokenRequest;
import com.reddit.clone.dto.RegisterRequest;
import com.reddit.clone.service.AuthService;
import com.reddit.clone.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody RegisterRequest registerReq) {
		authService.signup(registerReq);
		return new ResponseEntity<>("User Resgistration Successful", HttpStatus.OK);
	}
	
	@GetMapping("/account-verification/{token}")
	public ResponseEntity<?> accountVerification(@PathVariable("token") String verificationToken) {
		authService.verifyAccount(verificationToken);
		return new ResponseEntity<>("Account Activated Sucessfully.", HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginReq) {
		return authService.login(loginReq);
	}
	
	@PostMapping("/refresh/token")
	public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenReq) {
		return authService.refreshToken(refreshTokenReq);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenReq) {
		refreshTokenService.deleteRefreshToken(refreshTokenReq.getRefreshToken());
		return new ResponseEntity<>("Refresh token deleted successfully", HttpStatus.OK);
	}

}
