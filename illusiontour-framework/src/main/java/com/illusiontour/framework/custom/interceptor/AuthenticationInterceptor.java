package com.illusiontour.framework.custom.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.login.LoginUser;
import com.illusiontour.common.login.LoginUserHolder;
import com.illusiontour.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("access-token");

        // 解析该token，如果成功则放行，如果失败，则拦截。因为在parseToken中抛出异常，所以这里不需要显式拦截。
        Claims claims = JwtUtil.parseToken(token);

        // 使用 TypeReference 确保类型安全
        SysUser sysUser = objectMapper.readValue(claims.get("user", String.class), SysUser.class);
        Set<String> perms = objectMapper.readValue(claims.get("perms", String.class), objectMapper.getTypeFactory().constructCollectionType(Set.class, String.class));


        LoginUserHolder.setLoginUser(new LoginUser(sysUser, perms)); // 将loginUser放入threadlocal中。

        return true;
    }
}