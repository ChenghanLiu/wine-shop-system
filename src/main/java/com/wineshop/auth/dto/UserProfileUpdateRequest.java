package com.wineshop.auth.dto;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String nickname;
    private String phone;
}
