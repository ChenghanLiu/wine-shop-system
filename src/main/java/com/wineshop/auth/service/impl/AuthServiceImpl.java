package com.wineshop.auth.service.impl;

import com.wineshop.auth.dto.AdminLoginRequest;
import com.wineshop.auth.dto.UserLoginRequest;
import com.wineshop.auth.dto.UserRegisterRequest;
import com.wineshop.auth.service.AuthService;
import com.wineshop.auth.vo.CurrentUserInfoResponse;
import com.wineshop.auth.vo.LoginResponse;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.ResultCode;
import com.wineshop.common.security.JwtUtil;
import com.wineshop.common.security.SecurityConstants;
import com.wineshop.common.security.UserContext;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;

    public AuthServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(UserRegisterRequest request) {
        // TODO: implement register logic by ws_user table.
    }

    @Override
    public LoginResponse userLogin(UserLoginRequest request) {
        // TODO: validate user from DB and password check.
        String token = jwtUtil.generateToken(10001L, request.getUsername(), SecurityConstants.ROLE_USER);
        return new LoginResponse(token, SecurityConstants.TOKEN_PREFIX.trim(), 10001L, request.getUsername(), SecurityConstants.ROLE_USER);
    }

    @Override
    public LoginResponse adminLogin(AdminLoginRequest request) {
        // TODO: validate admin from DB and password check.
        String token = jwtUtil.generateToken(1L, request.getUsername(), SecurityConstants.ROLE_ADMIN);
        return new LoginResponse(token, SecurityConstants.TOKEN_PREFIX.trim(), 1L, request.getUsername(), SecurityConstants.ROLE_ADMIN);
    }

    @Override
    public CurrentUserInfoResponse currentUser() {
        UserContext.LoginUser loginUser = UserContext.get();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return new CurrentUserInfoResponse(loginUser.getId(), loginUser.getUsername(), loginUser.getRole());
    }
}
