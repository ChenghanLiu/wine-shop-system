package com.wineshop.category.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsCategory {
    private Long id;
    private String name;
    private Integer sort;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
