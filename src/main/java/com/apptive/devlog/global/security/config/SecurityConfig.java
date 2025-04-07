package com.apptive.devlog.global.security.config;

import com.apptive.devlog.global.security.filter.JwtAuthenticationFilter;
import com.apptive.devlog.domain.oauth2.handler.OAuth2FailureHandler;
import com.apptive.devlog.domain.oauth2.handler.OAuth2SuccessHandler;
import com.apptive.devlog.domain.oauth2.service.CustomOAuth2UserService;
import com.apptive.devlog.global.security.service.CustomUserDetailsService;
import com.apptive.devlog.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final HttpSession httpSession;
    // private final CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;
    // private final CustomAuthorizationCodeTokenResponseClient customAuthorizationCodeTokenResponseClient;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService, httpSession);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/signup", "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/auth/logout", "/user/profile").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        // .loginPage("/login")
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorization")/* .authorizationRequestResolver(customOAuth2AuthorizationRequestResolver) */)
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        // .tokenEndpoint().accessTokenResponseClient(customAuthorizationCodeTokenResponseClient)
                        .userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/*
 * http://localhost:8080/oauth2/authorization/google
 * https://accounts.google.com/o/oauth2/v2/auth
 * http://localhost:8080/login/oauth2/code/google
 * https://oauth2.googleapis.com/token
 * https://www.googleapis.com/oauth2/v3/userinfo
 * CustomOAuth2UserService
 * OAuth2AuthenticationSuccessHandler
 */
