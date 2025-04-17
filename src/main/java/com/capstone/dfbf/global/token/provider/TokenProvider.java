package com.capstone.dfbf.global.token.provider;


import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.token.vo.TokenResponse;

public interface TokenProvider {
    TokenResponse generateToken(AuthenticatedMember authenticatedMember);
}
