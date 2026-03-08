package com.wineshop.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateRequest {
    @Min(1)
    private Integer quantity;
    private Integer selected;
}
