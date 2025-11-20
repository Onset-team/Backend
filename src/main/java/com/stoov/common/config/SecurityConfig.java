package com.stoov.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stoov.common.exception.ErrorCode;
import com.stoov.common.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.OutputStream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.httpBasic(httpBasic -> httpBasic.disable())
			.formLogin(formLogin -> formLogin.disable())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.GET, "/api/places/search").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/places/{placeId}").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/users/google").permitAll()
				.requestMatchers(HttpMethod.GET, "/google-login-test.html").permitAll()
				.anyRequest().authenticated()
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
}
