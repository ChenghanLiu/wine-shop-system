package com.wineshop.order.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;


@Data
public class WsOrderItem {
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long productId;
    private String productName;
    private String productCover;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
}
