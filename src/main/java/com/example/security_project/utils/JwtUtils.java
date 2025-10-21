package com.example.security_project.utils;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

import javax.crypto.SecretKey;

import com.example.security_project.exception.CustomJWTException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

/*
 * 1. JWT 토큰 생성
 * 2. JWT 토큰 검증
 */
public class JwtUtils {
    //키값은 30자 이상 사용할 것!!
    private static String key = "12345678901234567890123456789012345";

    
    // jwt 토큰 생성
    public static String generateToken(Map<String, Object> claims, int min){
       SecretKey secretKey = null;

        try {
            secretKey = Keys.hmacShaKeyFor(JwtUtils.key.getBytes("utf-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder()
            .setHeader(Map.of("typ", "JWT"))
            .setClaims(claims)
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
            .compact();
    }

    //JWT 토큰 검증
    public static Map<String, Object> validateToken(String token){

        Map<String, Object> claims = null;
        try{
            //Secretkey
            SecretKey secretKey = Keys.hmacShaKeyFor(JwtUtils.key.getBytes("utf-8"));

            claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        } catch(MalformedJwtException malformedJwtException){
            throw new CustomJWTException("Malformed");
        } catch(ExpiredJwtException expiredJwtException){
            throw new CustomJWTException("expired");
        } catch(InvalidClaimException invalidClaimException){
            throw new CustomJWTException("invaild");
        } catch (JwtException jwtException){
            throw new CustomJWTException("JWTError");
        } catch(Exception e){
            throw new CustomJWTException("JWTError");
        }

        return claims;

    }
    
}