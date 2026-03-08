package com.wineshop.order.controller;

import com.wineshop.common.result.Result;
import com.wineshop.order.dto.OrderCheckoutRequest;
import com.wineshop.order.entity.WsOrder;
import com.wineshop.order.service.OrderService;
import com.wineshop.order.vo.OrderDetailVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public Result<Map<String, Long>> checkout(@Valid @RequestBody OrderCheckoutRequest request) {
        Long orderId = orderService.checkout(request);
        return Result.ok(Map.of("orderId", orderId));
    }

    @GetMapping
    public Result<List<WsOrder>> list(@RequestParam(required = false) Integer status) {
        return Result.ok(orderService.listMine(status));
    }

    @GetMapping("/{id}")
    public Result<OrderDetailVO> detail(@PathVariable Long id) {
        return Result.ok(orderService.detail(id));
    }

    @PostMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id) {
        orderService.pay(id);
        return Result.ok();
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.ok();
    }

    @PostMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        orderService.confirm(id);
        return Result.ok();
    }
}
