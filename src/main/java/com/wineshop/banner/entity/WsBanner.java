package com.wineshop.banner.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsBanner {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sort;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
