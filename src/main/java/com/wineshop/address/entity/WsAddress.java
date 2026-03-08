package com.wineshop.address.entity;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class WsAddress {
    private Long id;
    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
