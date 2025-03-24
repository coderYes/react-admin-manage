package com.illusiontour.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

public class JwtUtil {
    private static final long tokenExpiration = 60 * 60 * 24 * 7 * 1000L;
    private static final SecretKey tokenSignKey = Keys.hmacShaKeyFor("M0PKKI6pYGVWWfDZw90a0lTpGYX1d4AQ".getBytes());

    public static String createToken(SysUser sysUser, Set<String> perms) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(sysUser);
        String permsJson = objectMapper.writeValueAsString(perms);

        String token = Jwts.builder()
                .setSubject("USER_INFO")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("user", userJson)
                .claim("perms", permsJson)
                .signWith(tokenSignKey)
                .compact();
        return token;
    }

    public static Claims parseToken(String token) {
        if (token == null) {
            throw new TourException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {
            //jwt解析器
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(tokenSignKey).build();//为jwt解析器设置签名秘钥。
            //返回payload
            Jws<Claims> jwsClaims = jwtParser.parseClaimsJws(token);//解析token，得到jws（带有签名的jwt）
            return jwsClaims.getBody();
        } catch (ExpiredJwtException e) {
            throw new TourException(ResultCodeEnum.TOKEN_EXPIRED);//token过期
        } catch (JwtException e) {
            throw new TourException(ResultCodeEnum.TOKEN_INVALID);//token非法
        }
    }

}
