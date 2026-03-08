package com.wineshop.product.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVO {
    private Long id;
    private String name;
    private Long categoryId;
    private Long subcategoryId;
    private String coverImage;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String description;
    private Integer isRecommend;
}
