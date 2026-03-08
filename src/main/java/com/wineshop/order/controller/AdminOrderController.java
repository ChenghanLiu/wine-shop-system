package com.wineshop.order.controller;

import com.wineshop.common.result.Result;
import com.wineshop.order.entity.WsOrder;
import com.wineshop.order.service.OrderService;
import com.wineshop.order.vo.OrderDetailVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<List<WsOrder>> list(@RequestParam(required = false) Integer status) {
        return Result.ok(orderService.adminList(status));
    }

    @GetMapping("/{id}")
    public Result<OrderDetailVO> detail(@PathVariable Long id) {
        return Result.ok(orderService.adminDetail(id));
    }

    @PostMapping("/{id}/deliver")
    public Result<Void> deliver(@PathVariable Long id) {
        orderService.deliver(id);
        return Result.ok();
    }

    @PostMapping("/{id}/refund")
    public Result<Void> refund(@PathVariable Long id) {
        orderService.refund(id);
        return Result.ok();
    }
}
