package com.capstone.dfbf.global.oauth2.handler;

import com.capstone.dfbf.global.exception.error.ErrorCode;
import com.capstone.dfbf.global.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.INVALID_EMAIL_OR_PASSWORD.getCode())
                .message(ErrorCode.INVALID_TOKEN.getMessage())
                .displayType(ErrorCode.INVALID_EMAIL_OR_PASSWORD.getDisplayType())
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}