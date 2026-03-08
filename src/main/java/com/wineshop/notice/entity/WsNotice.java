package com.wineshop.notice.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsNotice {
    private Long id;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private LocalDateTime publishTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
