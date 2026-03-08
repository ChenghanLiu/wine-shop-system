package com.wineshop.order.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;


@Data
public class WsOrder {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime receiveTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
