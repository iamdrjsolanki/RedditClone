package com.reddit.clone.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.reddit.clone.exception.SpringRedditException;
import com.reddit.clone.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
	
	private KeyStore keyStore;
	
	@Value("${jwt.expiration.time}")
	private Long jwtExpirationTimeInMillis;
	
	@PostConstruct
	public void init() {
		try {
			keyStore = keyStore.getInstance("JKS");
			InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
			keyStore.load(resourceAsStream, "secret".toCharArray());
		} catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			throw new SpringRedditException("Exception occured while loading key store.");
		}
	}
	
	public String generateToken(Authentication authentication) {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
		return Jwts.builder()
					.setSubject(principal.getUsername())
					.signWith(getPrivateKey())
					.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
					.compact();
	}
	


	public String generateTokenWithUsername(String username) {
		return Jwts.builder()
				.setSubject(username)
				.signWith(getPrivateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTimeInMillis)))
				.compact();
	}

	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
		} catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			e.printStackTrace();
			throw new SpringRedditException("Exception occured while retrieving private key from keystore.");
		}
	}
	
	public boolean validateToken(String jwt) {
		Jwts.parser()
			.setSigningKey(getPublicKey())
			.parseClaimsJws(jwt);
		
		return true;
	}

	private PublicKey getPublicKey() {
		try {
			return (PublicKey) keyStore.getCertificate("springblog").getPublicKey();
		} catch(KeyStoreException e) {
			e.printStackTrace();
			throw new SpringRedditException("Exception occured while retrieving public key from keystore.");
		}
	}
	
	public String getUsernameFromJwt(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt).getBody();
		return claims.getSubject();
	}
	
	public Long getExpirationTime() {
		return jwtExpirationTimeInMillis;
	}
	
}
