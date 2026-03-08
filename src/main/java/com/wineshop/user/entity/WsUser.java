package com.wineshop.user.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
