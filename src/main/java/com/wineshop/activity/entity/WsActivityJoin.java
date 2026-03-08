package com.wineshop.activity.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsActivityJoin {
    private Long id;
    private Long activityId;
    private Long userId;
    private LocalDateTime createdAt;
}
