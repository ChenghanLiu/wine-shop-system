package com.wineshop.auth.service;

import com.wineshop.auth.dto.AdminLoginRequest;
import com.wineshop.auth.dto.UserLoginRequest;
import com.wineshop.auth.dto.UserProfileUpdateRequest;
import com.wineshop.auth.dto.UserRegisterRequest;
import com.wineshop.auth.vo.CurrentUserInfoResponse;
import com.wineshop.auth.vo.LoginResponse;

public interface AuthService {
    void register(UserRegisterRequest request);

    LoginResponse userLogin(UserLoginRequest request);

    LoginResponse adminLogin(AdminLoginRequest request);

    CurrentUserInfoResponse currentUser();

    void updateCurrentUser(UserProfileUpdateRequest request);

    void changeAdminPassword(String oldPassword, String newPassword);
}
