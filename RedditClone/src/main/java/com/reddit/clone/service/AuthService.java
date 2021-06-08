package com.reddit.clone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.clone.config.JwtProvider;
import com.reddit.clone.dao.UserRepository;
import com.reddit.clone.dao.VerificationTokenRepository;
import com.reddit.clone.dto.AuthenticationResponse;
import com.reddit.clone.dto.LoginRequest;
import com.reddit.clone.dto.RefreshTokenRequest;
import com.reddit.clone.dto.RegisterRequest;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.exception.UsernameNotFoundException;
import com.reddit.clone.model.NotificationEmail;
import com.reddit.clone.model.User;
import com.reddit.clone.model.VerificationToken;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepo;
	private final VerificationTokenRepository verificationTokenRepo;
	private final MailService mailService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Transactional
	public void signup(RegisterRequest registerReq) {
		User user = new User();
		user.setUsername(registerReq.getUsername());
		user.setEmail(registerReq.getEmail());
		user.setPassword(passwordEncoder.encode(registerReq.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepo.save(user);
		
		String verificationToken = generateVerificationToken(user);
		
		mailService.sendMail(
				new NotificationEmail(
						"Please Activate your Acount", 
						user.getEmail(), 
						"Thank you for signing up to Reddit Clone Web App, " + 
						"please click on the link below to activate your account " + 
						"http://localhost:8080/reddit-service/api/auth/account-verification/" + verificationToken
					)
			);
		
	}
	
	@Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
	
	private String generateVerificationToken(User user) {
		String verificationToken = UUID.randomUUID().toString();
		
		//store it in db for later use by the user to activate account
		VerificationToken token = new VerificationToken();
		token.setUser(user);
		token.setToken(verificationToken);
		
		verificationTokenRepo.save(token);
		
		return verificationToken;
	}

	public void verifyAccount(String verificationToken) {
		Optional<VerificationToken> verificationTokendb = verificationTokenRepo.findByToken(verificationToken);
		
		verificationTokendb.orElseThrow(() -> new SpringRedditException("Invalid Token."));
		
		fetchUserAndEnable(verificationTokendb.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();		
		
		User user = userRepo.findByUsername(username)
						.orElseThrow(() -> new SpringRedditException(username + " user not found."));
		
		user.setEnabled(true);
		
		userRepo.save(user);
	}

	public AuthenticationResponse login(LoginRequest loginReq) {
		Authentication authenticate = authenticationManager
			.authenticate(
					new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()
				)
			);
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		
		String jwtToken = jwtProvider.generateToken(authenticate);
		return AuthenticationResponse.builder()
				.username(loginReq.getUsername())
				.authenticationToken(jwtToken)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()))
				.build();
	}
	
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !(authentication instanceof AnonymousAuthenticationToken && authentication.isAuthenticated());
	}

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenReq) {
		refreshTokenService.validateRefreshToken(refreshTokenReq.getRefreshToken());
		String token = jwtProvider.generateTokenWithUsername(refreshTokenReq.getUsername());
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.username(refreshTokenReq.getUsername())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getExpirationTime()))
				.refreshToken(refreshTokenReq.getRefreshToken())
				.build();
	}

}
