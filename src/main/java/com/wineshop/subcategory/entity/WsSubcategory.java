package com.wineshop.subcategory.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsSubcategory {
    private Long id;
    private Long categoryId;
    private String name;
    private Integer sort;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
