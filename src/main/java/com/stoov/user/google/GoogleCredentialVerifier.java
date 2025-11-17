package com.stoov.user.google;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stoov.user.dto.GoogleOAuthRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class GoogleCredentialVerifier {
    private static final String GOOGLE_ISSUER = "https://accounts.google.com";

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    private final JwkProvider jwkProvider = createJwkProvider();

    private static JwkProvider createJwkProvider() {
        try {
            return new UrlJwkProvider(new URL("https://www.googleapis.com/oauth2/v3/certs"));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid Google JWKS URL", e);
        }
    }

    public GoogleCredentialPayload verify(GoogleOAuthRequest request){

        String idToken = request.getCredential();
        if (idToken == null) {
            throw new IllegalArgumentException("Google credential이 비어있습니다.");
        }
        try {
            DecodedJWT decodedJWT = JWT.decode(idToken);

            // 1) kid 로 JWK 찾기
            Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAKey) jwk.getPublicKey());

            // 2) 기본 검증 (서명 + iss + aud + exp)
            algorithm.verify(decodedJWT); // 서명 검증

            if (!decodedJWT.getIssuer().equals(GOOGLE_ISSUER)) {
                throw new RuntimeException("Google issuer(iss) 불일치");
            }

            if (!decodedJWT.getAudience().contains(googleClientId)) {
                throw new RuntimeException("clientId(aud) 불일치");
            }

            // exp(만료시간) 기본 검증
            if (decodedJWT.getExpiresAt().before(new Date())) {
                throw new RuntimeException("ID Token이 만료되었습니다.");
            }

            // 3) payload 추출
            String sub = decodedJWT.getClaim("sub").asString();
            String email = decodedJWT.getClaim("email").asString();
            String profileImageUrl = decodedJWT.getClaim("picture").asString();

            return GoogleCredentialPayload.builder()
                    .email(email)
                    .sub(sub)
                    .profileImageUrl(profileImageUrl)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Google ID Token 검증 실패: " + e.getMessage());
        }
    }
}
