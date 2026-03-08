package com.wineshop.comment.vo;

import lombok.Data;

@Data
public class CommentableOrderItemVO {
    private Long orderId;
    private Long orderItemId;
    private Long productId;
}
