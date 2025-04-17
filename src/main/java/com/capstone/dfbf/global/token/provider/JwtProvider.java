package com.capstone.dfbf.global.token.provider;

import com.capstone.dfbf.global.exception.BaseException;
import com.capstone.dfbf.global.security.domain.AuthenticatedMember;
import com.capstone.dfbf.global.token.vo.AccessTokenVO;
import com.capstone.dfbf.global.token.vo.RefreshTokenVO;
import com.capstone.dfbf.global.token.vo.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static com.capstone.dfbf.global.exception.error.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.capstone.dfbf.global.exception.error.ErrorCode.INVALID_TOKEN;
import static com.capstone.dfbf.global.properties.JwtProperties.ACCESS_TOKEN_EXPIRE_TIME;
import static com.capstone.dfbf.global.properties.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME;


@Getter
@Component
@Slf4j
public class JwtProvider implements TokenProvider {

    private final SecretKey SECRET_KEY;
    private final String ISS = "leedonghoon";

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey
    ) {
        byte[] keyBytes = Base64.getDecoder()
                .decode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.SECRET_KEY = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public TokenResponse generateToken(AuthenticatedMember authenticatedMember){
        AccessTokenVO accessToken = generateAccessToken(authenticatedMember);
        RefreshTokenVO refreshToken = generateRefreshToken(authenticatedMember);
        return new TokenResponse(accessToken, refreshToken);
    }

    public AccessTokenVO generateAccessToken(AuthenticatedMember authenticatedMember) {
        if (authenticatedMember.getEmail() == null ||
                authenticatedMember.getEmail().isBlank()) {
            return AccessTokenVO.of("");
        }
        return this.generateAccessToken(authenticatedMember.getEmail());
    }

    private AccessTokenVO generateAccessToken(String email) {
        String token = Jwts.builder()
                .claim("type", "access")
                .issuer(ISS)
                .audience().add(email).and()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SECRET_KEY)
                .compact();
        log.info("[generateAccessToken] {}", token);
        return AccessTokenVO.of(token);
    }

    public RefreshTokenVO generateRefreshToken(AuthenticatedMember authenticatedMember) {
        if (authenticatedMember.getEmail() == null || authenticatedMember.getEmail().isBlank()) {
            return RefreshTokenVO.of("");
        }
        return this.generateRefreshToken(authenticatedMember.getEmail());
    }

    private RefreshTokenVO generateRefreshToken(String email) {
        String token = Jwts.builder()
                .claim("type", "refresh")
                .issuer(ISS)
                .audience().add(email).and()
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SECRET_KEY)
                .compact();
        log.info("[generateRefreshToken] {}", token);
        return RefreshTokenVO.of(token);
    }

    public String parseAudience(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);

            if (claims.getPayload()
                    .getExpiration()
                    .before(new Date())) {
                throw BaseException.from(EXPIRED_ACCESS_TOKEN);
            }
            String aud = claims.getPayload()
                    .getAudience()
                    .iterator()
                    .next();

            return aud;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("[parseAudience] {} :{}", INVALID_TOKEN, token);
            throw BaseException.from(INVALID_TOKEN);
        } catch (BaseException e) {
            log.warn("[parseAudience] {} :{}", EXPIRED_ACCESS_TOKEN, token);
            throw BaseException.from(EXPIRED_ACCESS_TOKEN);
        }
    }
}
