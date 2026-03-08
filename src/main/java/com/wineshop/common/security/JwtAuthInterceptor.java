package com.wineshop.common.security;

import com.wineshop.common.result.ResultCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = resolveToken(request);
        if (token == null) {
            response.sendError(ResultCode.UNAUTHORIZED.getCode(), "Missing token");
            return false;
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            UserContext.set(buildLoginUser(claims));
            return true;
        } catch (Exception e) {
            response.sendError(ResultCode.UNAUTHORIZED.getCode(), "Invalid token");
            return false;
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return null;
        }
        return auth.substring(SecurityConstants.TOKEN_PREFIX.length());
    }

    private UserContext.LoginUser buildLoginUser(Claims claims) {
        String role = claims.get("role", String.class);
        if (!SecurityConstants.ROLE_USER.equals(role) && !SecurityConstants.ROLE_ADMIN.equals(role)) {
            throw new IllegalArgumentException("Unsupported role in token");
        }
        return new UserContext.LoginUser(
                claims.get("id", Number.class).longValue(),
                claims.get("username", String.class),
                role
        );
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
