package com.hospital.apigateway.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(ex -> ex
						// public: register, login
						.pathMatchers("/api/auth/**").permitAll()
						// public: anyone can browse hospitals and doctors
						.pathMatchers(HttpMethod.GET, "/api/hospitals/**", "/api/doctors/**").permitAll()
						// only these roles can change data
						.pathMatchers("/api/hospitals/**").hasRole("HOSPITAL")
						.pathMatchers("/api/doctors/**").hasAnyRole("HOSPITAL", "DOCTOR")
						.pathMatchers("/api/patients/**").hasRole("PATIENT")
						.pathMatchers("/api/reception/**").hasRole("HOSPITAL")
						// appointments: anyone logged in can read, only a patient can create/edit/cancel
						.pathMatchers(HttpMethod.GET, "/api/appointments/**").authenticated()
						.pathMatchers("/api/appointments/**").hasRole("PATIENT")
						.anyExchange().authenticated())
				.oauth2ResourceServer(oauth -> oauth
						.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		return http.build();
	}

	// Verifies the token signature with the SAME secret that auth-service used to sign it
	@Bean
	public ReactiveJwtDecoder jwtDecoder(@Value("${jwt.secret}") String secret) {
		SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		return NimbusReactiveJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
	}

	// Turns the token's "role" claim (e.g. PATIENT) into the authority ROLE_PATIENT used by hasRole(...)
	private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
		authorities.setAuthoritiesClaimName("role");
		authorities.setAuthorityPrefix("ROLE_");

		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(authorities);
		return new ReactiveJwtAuthenticationConverterAdapter(converter);
	}
}