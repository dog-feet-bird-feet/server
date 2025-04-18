package com.capstone.dfbf.api.annotation;

import com.capstone.dfbf.api.member.Member;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {

    long memberId() default 1L;

    Member.Role role() default Member.Role.USER;
}
