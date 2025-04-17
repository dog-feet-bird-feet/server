package com.capstone.dfbf.global.oauth2.handler;

import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import com.capstone.dfbf.global.token.provider.JwtProvider;
import com.capstone.dfbf.global.token.vo.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String accessToken = tokenResponse.accessToken().token();

        Map<String, Object> body = new HashMap<>();
        body.put("accessToken", accessToken);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }
}
