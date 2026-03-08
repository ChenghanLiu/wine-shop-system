package com.wineshop.cart.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsCart {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Integer selected;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
