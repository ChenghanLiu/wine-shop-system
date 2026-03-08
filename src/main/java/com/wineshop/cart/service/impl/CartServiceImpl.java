package com.wineshop.cart.service.impl;

import com.wineshop.cart.dto.CartItemCreateRequest;
import com.wineshop.cart.dto.CartItemUpdateRequest;
import com.wineshop.cart.entity.WsCart;
import com.wineshop.cart.mapper.WsCartMapper;
import com.wineshop.cart.service.CartService;
import com.wineshop.common.security.AuthHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final WsCartMapper wsCartMapper;

    public CartServiceImpl(WsCartMapper wsCartMapper) {
        this.wsCartMapper = wsCartMapper;
    }

    @Override
    public List<WsCart> listMine() {
        Long userId = AuthHelper.currentUser().getId();
        return wsCartMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public void addItem(CartItemCreateRequest request) {
        Long userId = AuthHelper.currentUser().getId();
        WsCart existed = wsCartMapper.selectByUserIdAndProductId(userId, request.getProductId());
        if (existed != null) {
            existed.setQuantity(existed.getQuantity() + request.getQuantity());
            if (request.getSelected() != null) {
                existed.setSelected(request.getSelected());
            }
            wsCartMapper.updateByIdAndUserId(existed);
            return;
        }

        WsCart cart = new WsCart();
        cart.setUserId(userId);
        cart.setProductId(request.getProductId());
        cart.setQuantity(request.getQuantity());
        cart.setSelected(request.getSelected() == null ? 1 : request.getSelected());
        wsCartMapper.insert(cart);
    }

    @Override
    public void updateItem(Long id, CartItemUpdateRequest request) {
        Long userId = AuthHelper.currentUser().getId();
        WsCart existed = wsCartMapper.selectByIdAndUserId(id, userId);
        if (existed == null) {
            return;
        }
        if (request.getQuantity() != null) {
            existed.setQuantity(request.getQuantity());
        }
        if (request.getSelected() != null) {
            existed.setSelected(request.getSelected());
        }
        wsCartMapper.updateByIdAndUserId(existed);
    }

    @Override
    public void deleteItem(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        wsCartMapper.deleteByIdAndUserId(id, userId);
    }
}
