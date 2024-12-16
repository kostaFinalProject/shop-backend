package com.example.shop.config;

import com.example.shop.domain.instagram.Grade;
import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.instagram.Provider;
import com.example.shop.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Provider providerEnum = Provider.valueOf(provider.toUpperCase());

        String email = extractEmail(oAuth2User, provider);

        Member member = memberRepository.findByEmailAndProvider(email, providerEnum)
                .orElseGet(() -> createSocialMember(email, providerEnum));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                oAuth2User.getAttributes(),
                "id"
        );
    }

    public OAuth2User loadUserFromProvider(String accessToken, String provider) {
        String url = getUserInfoUri(provider);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> attributes = response.getBody();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes,
                "id"
        );
    }

    public Member createSocialMember(String email, Provider provider) {
        String userId = RandomStringUtils.randomAlphanumeric(8);
        String nickname = provider.name() + "_" + RandomStringUtils.randomAlphanumeric(8);

        Member newMember = Member.createMember(
                userId,
                null,
                provider.name() + " USER",
                nickname,
                email,
                null,
                null,
                Grade.USER,
                provider
        );

        return memberRepository.save(newMember);
    }

    private String extractEmail(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                return kakaoAccount.get("email").toString();
            case "naver":
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                return response.get("email").toString();
            case "google":
                return oAuth2User.getAttributes().get("email").toString();
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }

    private String getUserInfoUri(String provider) {
        switch (provider) {
            case "kakao":
                return "https://kapi.kakao.com/v2/user/me";
            case "naver":
                return "https://openapi.naver.com/v1/nid/me";
            case "google":
                return "https://www.googleapis.com/oauth2/v3/userinfo";
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}
