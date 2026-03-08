package com.wineshop.cart.service;

import com.wineshop.cart.dto.CartItemCreateRequest;
import com.wineshop.cart.dto.CartItemUpdateRequest;
import com.wineshop.cart.entity.WsCart;

import java.util.List;

public interface CartService {
    List<WsCart> listMine();

    void addItem(CartItemCreateRequest request);

    void updateItem(Long id, CartItemUpdateRequest request);

    void deleteItem(Long id);
}
