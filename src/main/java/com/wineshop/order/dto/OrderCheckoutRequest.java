package com.wineshop.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderCheckoutRequest {
    @NotNull
    private Long addressId;
    private String remark;
}
