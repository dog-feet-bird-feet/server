package com.capstone.dfbf.global.security.config;

import com.capstone.dfbf.global.oauth2.handler.OAuthFailureHandler;
import com.capstone.dfbf.global.oauth2.handler.OAuthLoginSuccessHandler;
import com.capstone.dfbf.global.oauth2.service.CustomOAuth2Service;
import com.capstone.dfbf.global.security.filter.JwtFilter;
import com.capstone.dfbf.global.security.service.MemberDetailsService;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final MemberDetailsService memberDetailsService;
    private final OAuthLoginSuccessHandler oAuthSuccessHandler;
    private final OAuthFailureHandler oAuthFailureHandler;
    private final CustomOAuth2Service customOAuth2Service;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/oauth2/**", "/index/**", "/index.js", "/favicon.ico",
                                "/templates", "/api/v1/s3/**", "/error").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::permitAll)
                .requestCache(RequestCacheConfigurer::disable)
                .securityContext(securityContext -> {
                    securityContext
                            .requireExplicitSave(true);
                })
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(c -> c
                                .userService(customOAuth2Service))
                        .successHandler(oAuthSuccessHandler)
                        .failureHandler(oAuthFailureHandler))
                .addFilterBefore(jwtAuthFilter(), OAuth2LoginAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtFilter jwtAuthFilter() {
        return new JwtFilter(jwtProvider, memberDetailsService);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
