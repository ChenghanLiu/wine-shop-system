package com.wineshop.subcategory.vo;

import lombok.Data;

@Data
public class SubcategoryVO {
    private Long id;
    private Long categoryId;
    private String name;
    private Integer sort;
}
