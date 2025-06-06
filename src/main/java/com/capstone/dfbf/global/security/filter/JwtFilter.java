package com.capstone.dfbf.global.security.filter;

import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.properties.JwtProperties;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import com.capstone.dfbf.global.security.service.MemberDetailsService;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.capstone.dfbf.global.exception.error.ErrorCode.EMPTY_TOKEN_PROVIDED;
import static com.capstone.dfbf.global.properties.JwtProperties.ACCESS_TOKEN_HEADER_NAME;
import static com.capstone.dfbf.global.properties.JwtProperties.ACCESS_TOKEN_TYPE;


@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberDetailsService memberDetailsService;
    private final SecurityContextRepository securityContextRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtHeader = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
        if (jwtHeader == null || !jwtHeader.startsWith(ACCESS_TOKEN_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = resolveToken(request);
        String aud = jwtProvider.parseAudience(token);
        PrincipalDetails principalDetails = memberDetailsService.loadUserByUsername(aud);
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                principalDetails,
                null,
                principalDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String authorization = httpServletRequest.getHeader(ACCESS_TOKEN_HEADER_NAME);
        if (authorization == null) {
            throw BaseException.from(EMPTY_TOKEN_PROVIDED);
        }
        if (authorization.startsWith(ACCESS_TOKEN_TYPE)) {
            return authorization.substring(7);
        }
        throw BaseException.from(EMPTY_TOKEN_PROVIDED);
    }
}
