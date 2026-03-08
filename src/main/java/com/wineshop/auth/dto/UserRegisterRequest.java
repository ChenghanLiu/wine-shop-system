package com.wineshop.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String nickname;
    private String phone;
}
