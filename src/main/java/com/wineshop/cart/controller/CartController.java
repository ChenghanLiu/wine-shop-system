package com.wineshop.cart.controller;

import com.wineshop.cart.dto.CartItemCreateRequest;
import com.wineshop.cart.dto.CartItemUpdateRequest;
import com.wineshop.cart.entity.WsCart;
import com.wineshop.cart.service.CartService;
import com.wineshop.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Result<List<WsCart>> list() {
        return Result.ok(cartService.listMine());
    }

    @PostMapping("/items")
    public Result<Void> addItem(@Valid @RequestBody CartItemCreateRequest request) {
        cartService.addItem(request);
        return Result.ok();
    }

    @PutMapping("/items/{id}")
    public Result<Void> updateItem(@PathVariable Long id, @Valid @RequestBody CartItemUpdateRequest request) {
        cartService.updateItem(id, request);
        return Result.ok();
    }

    @DeleteMapping("/items/{id}")
    public Result<Void> deleteItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return Result.ok();
    }
}
