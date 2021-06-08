package com.reddit.clone.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.reddit.clone.dao.RefreshTokenRepository;
import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.RefreshToken;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepo;
	
	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		
		return refreshTokenRepo.save(refreshToken);
	}
	
	public void validateRefreshToken(String token) {
		refreshTokenRepo.findByToken(token)
			.orElseThrow(() -> new SpringRedditException("Unable to valid the token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepo.deleteByToken(token);
	}

}
