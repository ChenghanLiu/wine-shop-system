package com.wineshop.comment.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsComment {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long userId;
    private Long productId;
    private Integer score;
    private String content;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
