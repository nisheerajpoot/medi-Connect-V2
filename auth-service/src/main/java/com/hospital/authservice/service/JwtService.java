package com.hospital.authservice.service;


import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

// Creates the JWT. The same secret is used by the API Gateway to verify the token.
@Service
public class JwtService {

	private final byte[] secret;
	private final long expirationMinutes;

	public JwtService(@Value("${jwt.secret}") String secret,
			@Value("${jwt.expiration-minutes}") long expirationMinutes) {
		byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
		if (bytes.length < 32) {
			throw new IllegalStateException("jwt.secret must be at least 32 characters long");
		}
		this.secret = bytes;
		this.expirationMinutes = expirationMinutes;
	}

	public String generateToken(Long userId, String email, String role, Long profileId, String name) {
		Instant now = Instant.now();

		JWTClaimsSet claims = new JWTClaimsSet.Builder()
				.subject(String.valueOf(userId))
				.issuer("mediconnect-auth-service")
				.claim("email", email)
				.claim("role", role)
				.claim("profileId", profileId)
				.claim("name", name)
				.issueTime(Date.from(now))
				.expirationTime(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
				.build();

		try {
			SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
			jwt.sign(new MACSigner(secret));
			return jwt.serialize();
		} catch (JOSEException e) {
			throw new IllegalStateException("Could not create the JWT", e);
		}
	}
}