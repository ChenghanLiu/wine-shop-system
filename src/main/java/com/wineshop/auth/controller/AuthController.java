package com.wineshop.auth.controller;

import com.wineshop.auth.dto.AdminLoginRequest;
import com.wineshop.auth.dto.UserLoginRequest;
import com.wineshop.auth.dto.UserRegisterRequest;
import com.wineshop.auth.service.AuthService;
import com.wineshop.auth.vo.CurrentUserInfoResponse;
import com.wineshop.auth.vo.LoginResponse;
import com.wineshop.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterRequest request) {
        authService.register(request);
        return Result.ok();
    }

    @PostMapping("/auth/login")
    public Result<LoginResponse> userLogin(@Valid @RequestBody UserLoginRequest request) {
        return Result.ok(authService.userLogin(request));
    }

    @GetMapping("/auth/me")
    public Result<CurrentUserInfoResponse> me() {
        return Result.ok(authService.currentUser());
    }

    @PostMapping("/admin/auth/login")
    public Result<LoginResponse> adminLogin(@Valid @RequestBody AdminLoginRequest request) {
        return Result.ok(authService.adminLogin(request));
    }
}
