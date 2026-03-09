package com.wineshop.auth.service.impl;

import com.wineshop.admin.entity.WsAdmin;
import com.wineshop.admin.mapper.WsAdminMapper;
import com.wineshop.auth.dto.AdminLoginRequest;
import com.wineshop.auth.dto.UserLoginRequest;
import com.wineshop.auth.dto.UserProfileUpdateRequest;
import com.wineshop.auth.dto.UserRegisterRequest;
import com.wineshop.auth.service.AuthService;
import com.wineshop.auth.vo.CurrentUserInfoResponse;
import com.wineshop.auth.vo.LoginResponse;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.ResultCode;
import com.wineshop.common.security.JwtUtil;
import com.wineshop.common.security.SecurityConstants;
import com.wineshop.common.security.UserContext;
import com.wineshop.user.entity.WsUser;
import com.wineshop.user.mapper.WsUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final WsUserMapper wsUserMapper;
    private final WsAdminMapper wsAdminMapper;

    public AuthServiceImpl(JwtUtil jwtUtil, WsUserMapper wsUserMapper, WsAdminMapper wsAdminMapper) {
        this.jwtUtil = jwtUtil;
        this.wsUserMapper = wsUserMapper;
        this.wsAdminMapper = wsAdminMapper;
    }

    @Override
    @Transactional
    public void register(UserRegisterRequest request) {
        WsUser existed = wsUserMapper.selectByUsername(request.getUsername());
        if (existed != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Username already exists");
        }

        WsUser user = new WsUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setStatus(1);
        wsUserMapper.insert(user);
    }

    @Override
    public LoginResponse userLogin(UserLoginRequest request) {
        WsUser user = wsUserMapper.selectByUsername(request.getUsername());
        if (user == null || user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "User not found or disabled");
        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), SecurityConstants.ROLE_USER);
        return new LoginResponse(token, SecurityConstants.TOKEN_PREFIX.trim(), user.getId(), user.getUsername(), SecurityConstants.ROLE_USER);
    }

    @Override
    public LoginResponse adminLogin(AdminLoginRequest request) {
        WsAdmin admin = wsAdminMapper.selectByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (admin == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), SecurityConstants.ROLE_ADMIN);
        return new LoginResponse(token, SecurityConstants.TOKEN_PREFIX.trim(), admin.getId(), admin.getUsername(), SecurityConstants.ROLE_ADMIN);
    }

    @Override
    public CurrentUserInfoResponse currentUser() {
        UserContext.LoginUser loginUser = UserContext.get();
        if (loginUser == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        if (SecurityConstants.ROLE_ADMIN.equals(loginUser.getRole())) {
            WsAdmin admin = wsAdminMapper.selectById(loginUser.getId());
            if (admin == null || admin.getStatus() == null || admin.getStatus() != 1) {
                throw new BusinessException(ResultCode.UNAUTHORIZED);
            }
            return new CurrentUserInfoResponse(admin.getId(), admin.getUsername(), SecurityConstants.ROLE_ADMIN, null, null);
        }

        WsUser user = wsUserMapper.selectById(loginUser.getId());
        if (user == null || user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        return new CurrentUserInfoResponse(user.getId(), user.getUsername(), SecurityConstants.ROLE_USER, user.getNickname(), user.getPhone());
    }

    @Override
    public void updateCurrentUser(UserProfileUpdateRequest request) {
        UserContext.LoginUser loginUser = UserContext.get();
        if (loginUser == null || !SecurityConstants.ROLE_USER.equals(loginUser.getRole())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        WsUser user = new WsUser();
        user.setId(loginUser.getId());
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        if (wsUserMapper.updateProfileById(user) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "User not found");
        }
    }

    @Override
    public void changeAdminPassword(String oldPassword, String newPassword) {
        UserContext.LoginUser loginUser = UserContext.get();
        if (loginUser == null || !SecurityConstants.ROLE_ADMIN.equals(loginUser.getRole())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        WsAdmin admin = wsAdminMapper.selectById(loginUser.getId());
        if (admin == null || admin.getStatus() == null || admin.getStatus() != 1) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
        if (!admin.getPassword().equals(oldPassword)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Old password is incorrect");
        }
        wsAdminMapper.updatePasswordById(admin.getId(), newPassword);
    }
}

