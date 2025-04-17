package com.capstone.dfbf.api.member.repository;

import com.capstone.dfbf.api.member.Member;
import com.capstone.dfbf.global.oauth2.domain.OAuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findByOauthIdAndProviderType(String oauthId, OAuthProviderType providerType);
}
