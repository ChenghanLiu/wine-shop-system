package com.wineshop.activity.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsActivity {
    private Long id;
    private String title;
    private String coverImage;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
