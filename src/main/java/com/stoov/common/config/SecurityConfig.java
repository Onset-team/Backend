package com.stoov.common.config;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stoov.common.exception.ErrorCode;
import com.stoov.common.response.CustomApiResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;

	@Value("${app.cors.allowed-origins}")
	private List<String> allowedOrigins;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource)
		throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.formLogin(formLogin -> formLogin.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource))
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll()
			)
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint((request, response, authException) -> {
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					try (OutputStream os = response.getOutputStream()) {
						objectMapper.writeValue(os, CustomApiResponse.error(ErrorCode.UNAUTHORIZED));
						os.flush();
					} catch (IOException e) {
						// 로깅 필요
					}
				})
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setStatus(HttpStatus.FORBIDDEN.value());
					try (OutputStream os = response.getOutputStream()) {
						objectMapper.writeValue(os, CustomApiResponse.error(ErrorCode.FORBIDDEN));
						os.flush();
					} catch (IOException e) {
						// 로깅 필요
					}
				})
			);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
