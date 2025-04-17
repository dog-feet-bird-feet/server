package com.capstone.dfbf.global.token.repository;


import com.capstone.dfbf.global.token.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
