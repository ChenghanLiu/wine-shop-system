package com.wineshop.product.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsProductImage {
    private Long id;
    private Long productId;
    private String imageUrl;
    private Integer sort;
    private LocalDateTime createdAt;
}
