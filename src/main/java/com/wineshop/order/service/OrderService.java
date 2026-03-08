package com.wineshop.order.service;

import com.wineshop.order.dto.OrderCheckoutRequest;
import com.wineshop.order.entity.WsOrder;
import com.wineshop.order.vo.OrderDetailVO;

import java.util.List;

public interface OrderService {
    Long checkout(OrderCheckoutRequest request);

    List<WsOrder> listMine(Integer status);

    OrderDetailVO detail(Long id);

    void pay(Long id);

    void cancel(Long id);

    void confirm(Long id);

    void deliver(Long id);

    List<WsOrder> adminList(Integer status);

    OrderDetailVO adminDetail(Long id);

    void refund(Long id);
}
