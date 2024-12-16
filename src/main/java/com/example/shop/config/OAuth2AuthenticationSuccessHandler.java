package com.example.shop.config;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.instagram.Provider;
import com.example.shop.repository.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Value("${app.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = authToken.getPrincipal();

        String provider = authToken.getAuthorizedClientRegistrationId();
        Provider providerEnum = Provider.valueOf(provider.toUpperCase());

        String email = extractEmail(oAuth2User, provider);

        Member member = memberRepository.findByEmailAndProvider(email, providerEnum)
                .orElseThrow(() -> new IllegalArgumentException("소셜 로그인 사용자 정보가 없습니다."));

        String accessToken = jwtUtil.generateToken(member.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(member.getUserId());

        response.addHeader("Authorization", "Bearer" + accessToken);
        response.addHeader("Refresh-Token", "Bearer" + refreshToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    private String extractEmail(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                return kakaoAccount.get("email").toString();
            case "naver":
                Map<String, Object> naverResponse = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                return naverResponse.get("email").toString();
            case "google":
                return oAuth2User.getAttributes().get("email").toString();
            default:
                throw new IllegalArgumentException("지원하지 않는 provider입니다: " + provider);
        }
    }
}
