package com.capstone.dfbf.api.annotation;

import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.security.domain.PrincipalDetails;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        PrincipalDetails principalDetails = Mockito.mock(PrincipalDetails.class);
        AuthenticatedMember authenticatedMember = Mockito.mock(AuthenticatedMember.class);

        Mockito.when(principalDetails.getAuthenticatedMember()).thenReturn(authenticatedMember);
        Mockito.when(authenticatedMember.getMemberId()).thenReturn(annotation.memberId());
        Mockito.when(authenticatedMember.getRole()).thenReturn(annotation.role());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities()
        );

        context.setAuthentication(authentication);
        return context;
    }
}
