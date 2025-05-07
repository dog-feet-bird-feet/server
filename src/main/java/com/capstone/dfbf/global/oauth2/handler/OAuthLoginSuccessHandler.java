package com.capstone.dfbf.global.oauth2.handler;

import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import com.capstone.dfbf.global.token.vo.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Component
@Slf4j
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        AuthenticatedMember authMember = ((PrincipalDetails) authentication.getPrincipal()).getAuthenticatedMember();
        TokenResponse tokenResponse = jwtProvider.generateToken(authMember);
        sendJsonTokenResponse(request, response, tokenResponse);
    }

    private void sendJsonTokenResponse(HttpServletRequest request,
                                       HttpServletResponse response,
                                       TokenResponse tokenResponse) throws IOException {
        String accessToken = tokenResponse.accessToken().token();
//        response.sendRedirect("/home?accessToken=" + accessToken);
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax") // Lax, Strict, None 중 선택 가능
                .maxAge(Duration.ofHours(1))
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        response.sendRedirect("/");
    }
}
