package com.wineshop.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private Long id;
    private String username;
    private String role;
}
