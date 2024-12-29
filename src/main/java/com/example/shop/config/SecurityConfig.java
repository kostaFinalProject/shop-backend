package com.example.shop.config;

import com.example.shop.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, customUserDetailsService);
        http.
                csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/api/v1/members").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/members/check-id").permitAll()
                                .requestMatchers(HttpMethod.POST, "api/v1/members/check-nickname").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/members/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/members/oauth/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/members/refresh-token").permitAll()
                                .requestMatchers(HttpMethod.GET, "api/v1/members").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/profile/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/profile").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/members/profile").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/members/promotion/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/members/relegation/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/members").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/members/oauth").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/followers/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/followees/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/requests").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/blocks").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/items/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/members/logout").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/all").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/articles/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/article-collections").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/tokens/validate").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/item-categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/item-categories/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/item-categories")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/admin-request")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/items").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/items/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/items")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/items/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/items")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/items/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "api/v1/likes/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/articles").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/articles/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/articles/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/articles/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/comments/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/comments/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/comments/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/articles")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/articles/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/error").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/discounts/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/discounts/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/likes/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/likes/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/followers/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/followers/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/followers/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/members/account-status").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/article-collections/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/article-collections/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/blocks/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/blocks/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/carts").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/carts/orders").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/carts/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/carts").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/carts").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/wish-lists/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/wish-lists/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/wish-lists").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/orders").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/payments").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/payments/**").authenticated()
                                .requestMatchers(HttpMethod.POST,"/api/v1/delivery-address").authenticated()
                                .requestMatchers(HttpMethod.GET,  "/api/v1/delivery-address").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/delivery-address/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/qna").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/qna/answer")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/qna/question/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/v1/qna/answer/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/qna/question/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/qna/answer/**")
                                .hasAnyAuthority("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/qna/**").permitAll())
                .oauth2Login(oauth ->
                        oauth
                                .loginPage("http://localhost:3000/login")
                                .userInfoEndpoint(c -> c.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return new CustomUserDetailsService(memberRepository);  // CustomUserDetailsService 빈 등록
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // AuthenticationManager 빈 등록
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);  // CustomUserDetailsService 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());  // 비밀번호 암호화 설정
        return authProvider;
    }
}
