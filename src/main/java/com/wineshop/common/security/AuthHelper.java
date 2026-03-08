package com.wineshop.common.security;

import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.ResultCode;

import java.util.Arrays;

public final class AuthHelper {
    private AuthHelper() {
    }

    public static UserContext.LoginUser currentUser() {
        UserContext.LoginUser user = UserContext.get();
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Please login first");
        }
        return user;
    }

    public static void requireRole(String... roles) {
        String currentRole = currentUser().getRole();
        boolean matched = Arrays.stream(roles).anyMatch(role -> role.equals(currentRole));
        if (!matched) {
            throw new BusinessException(ResultCode.FORBIDDEN, "No permission");
        }
    }

    public static void requireAdmin() {
        requireRole(SecurityConstants.ROLE_ADMIN);
    }
}
