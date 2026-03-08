package com.wineshop.order.vo;

import com.wineshop.order.entity.WsOrder;
import com.wineshop.order.entity.WsOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDetailVO {
    private WsOrder order;
    private List<WsOrderItem> items;
}
