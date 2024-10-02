package com.wooil.ustar.Util.jwt;

import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private String secret;
    private long accessTokenValidity;
    private long refreshTokenValidity;
    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-validity}") long accessTokenValidity,
        @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
        this.secret = secret;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.access-token-validity}")
//    private long accessTokenValidity;
//
//    @Value("${jwt.refresh-token-validity}")
//    private long refreshTokenValidity;
//
//    private Key key;

//    @PostConstruct
//    public void init() {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }

    /*
      Access Token 생성 함수
      Subject에 username을 사용한다.

      username만 사용하는 것이 보안적으로 안전한지 확인해봐야함.
    */
    public String generateAccessToken(String userEmail) {
        return Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(key)
            .compact();
    }

    /*
      Refresh Token 생성 함수
      Subject에 username을 사용한다.
 
      username만 사용하는 것이 보안적으로 안전한지 확인해봐야함.
    */
    public String generateRefreshToken(String userEmail) {
        return Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
            .signWith(key)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_002);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TOKEN_001);
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
