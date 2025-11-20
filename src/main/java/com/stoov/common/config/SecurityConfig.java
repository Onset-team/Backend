package com.stoov.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
            // CSRF 보호 비활성화 (Stateless API에서는 일반적으로 비활성화)
            .csrf(csrf -> csrf.disable())
            // HTTP Basic 인증 비활성화
            .httpBasic(httpBasic -> httpBasic.disable())
            // 폼 로그인 비활성화
            .formLogin(formLogin -> formLogin.disable())
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // 경로별 인가 설정
            .authorizeHttpRequests(authorize -> authorize
                // 헬스체크는 항상 허용
                .requestMatchers("/actuator/health", "/health").permitAll()
                // 장소 검색 및 상세 조회는 누구나 접근 가능
                .requestMatchers(HttpMethod.GET, "/api/places").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/places/**").permitAll()
                // 마이페이지 조회는 로그인 여부와 상관없이 접근 가능 (비로그인 시 null 정보 반환)
                .requestMatchers(HttpMethod.GET, "/api/users/my").permitAll()
                // Google 로그인 경로는 누구나 접근 가능
                .requestMatchers(HttpMethod.POST, "/api/users/google").permitAll()
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
                //로컬 테스트용
                //.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://stoov.vercel.app",
            "http://localhost:5173"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
