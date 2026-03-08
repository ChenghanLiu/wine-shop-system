package com.wineshop.product.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;


@Data
public class WsProduct {
    private Long id;
    private String name;
    private Long categoryId;
    private Long subcategoryId;
    private String coverImage;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String description;
    private Integer status;
    private Integer isRecommend;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
