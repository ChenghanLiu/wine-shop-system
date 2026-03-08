package com.wineshop.admin.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsAdmin {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
