package com.capstone.dfbf.global.security.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final AuthenticatedMember authenticatedMember;
    private Map<String, Object> attributes;

    public PrincipalDetails(AuthenticatedMember authenticatedMember) {
        this.authenticatedMember = authenticatedMember;
    }

    public PrincipalDetails(AuthenticatedMember authenticatedMember,
                            Map<String, Object> attributes) {
        this.authenticatedMember = authenticatedMember;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authenticatedMember.getAuthorities();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return authenticatedMember.getEmail();
    }

    @Override
    public String getName() {
        return authenticatedMember.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static PrincipalDetails from(AuthenticatedMember authenticatedMember){
        return new PrincipalDetails(authenticatedMember);
    }

    public static PrincipalDetails from(AuthenticatedMember authenticatedMember,
                                        Map<String, Object> attributes){
        return new PrincipalDetails(authenticatedMember, attributes);
    }
}
