package com.wineshop.favorite.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsFavorite {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}
