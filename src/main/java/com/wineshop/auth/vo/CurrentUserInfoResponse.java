package com.wineshop.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUserInfoResponse {
    private Long id;
    private String username;
    private String role;
    private String nickname;
    private String phone;
}
