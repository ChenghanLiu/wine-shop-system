package com.wineshop.order.service.impl;

import com.wineshop.address.entity.WsAddress;
import com.wineshop.address.mapper.WsAddressMapper;
import com.wineshop.cart.entity.WsCart;
import com.wineshop.cart.mapper.WsCartMapper;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.ResultCode;
import com.wineshop.common.security.AuthHelper;
import com.wineshop.order.dto.OrderCheckoutRequest;
import com.wineshop.order.entity.WsOrder;
import com.wineshop.order.entity.WsOrderItem;
import com.wineshop.order.mapper.WsOrderItemMapper;
import com.wineshop.order.mapper.WsOrderMapper;
import com.wineshop.order.service.OrderService;
import com.wineshop.order.vo.OrderDetailVO;
import com.wineshop.product.entity.WsProduct;
import com.wineshop.product.mapper.WsProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    private final WsCartMapper wsCartMapper;
    private final WsAddressMapper wsAddressMapper;
    private final WsProductMapper wsProductMapper;
    private final WsOrderMapper wsOrderMapper;
    private final WsOrderItemMapper wsOrderItemMapper;

    public OrderServiceImpl(WsCartMapper wsCartMapper,
                            WsAddressMapper wsAddressMapper,
                            WsProductMapper wsProductMapper,
                            WsOrderMapper wsOrderMapper,
                            WsOrderItemMapper wsOrderItemMapper) {
        this.wsCartMapper = wsCartMapper;
        this.wsAddressMapper = wsAddressMapper;
        this.wsProductMapper = wsProductMapper;
        this.wsOrderMapper = wsOrderMapper;
        this.wsOrderItemMapper = wsOrderItemMapper;
    }

    @Override
    @Transactional
    public Long checkout(OrderCheckoutRequest request) {
        Long userId = AuthHelper.currentUser().getId();

        WsAddress address = wsAddressMapper.selectByIdAndUserId(request.getAddressId(), userId);
        if (address == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Invalid addressId");
        }

        List<WsCart> selectedCarts = wsCartMapper.selectSelectedByUserId(userId);
        if (selectedCarts == null || selectedCarts.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "No selected cart items");
        }

        WsOrder order = new WsOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStatus(0);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setProvince(address.getProvince());
        order.setCity(address.getCity());
        order.setDistrict(address.getDistrict());
        order.setDetailAddress(address.getDetailAddress());
        order.setRemark(request.getRemark());

        List<WsOrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (WsCart cart : selectedCarts) {
            WsProduct product = wsProductMapper.selectById(cart.getProductId());
            if (product == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "Product not found: " + cart.getProductId());
            }

            WsOrderItem item = new WsOrderItem();
            item.setOrderNo(order.getOrderNo());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductCover(product.getCoverImage());
            item.setProductPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            item.setTotalPrice(itemTotal);
            totalAmount = totalAmount.add(itemTotal);
            orderItems.add(item);
        }

        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        wsOrderMapper.insert(order);

        for (WsOrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        wsOrderItemMapper.batchInsert(orderItems);

        wsCartMapper.deleteSelectedByUserId(userId);
        return order.getId();
    }

    @Override
    public List<WsOrder> listMine(Integer status) {
        Long userId = AuthHelper.currentUser().getId();
        return wsOrderMapper.selectByUserIdAndStatus(userId, status);
    }

    @Override
    public OrderDetailVO detail(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        WsOrder order = wsOrderMapper.selectByIdAndUserId(id, userId);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Order not found");
        }
        List<WsOrderItem> items = wsOrderItemMapper.selectByOrderId(order.getId());
        return new OrderDetailVO(order, items);
    }

    @Override
    @Transactional
    public void pay(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        int updated = wsOrderMapper.payOrder(id, userId, LocalDateTime.now());
        if (updated == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Only pending payment orders can be paid");
        }
        increaseSalesByOrderId(id);
    }

    @Override
    public void cancel(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        int updated = wsOrderMapper.cancelOrder(id, userId);
        if (updated == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Only pending payment orders can be cancelled");
        }
    }

    @Override
    @Transactional
    public void confirm(Long id) {
        Long userId = AuthHelper.currentUser().getId();
        WsOrder order = wsOrderMapper.selectByIdAndUserId(id, userId);
        int updated = wsOrderMapper.confirmOrder(id, userId, LocalDateTime.now());
        if (updated == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Only pending receipt orders can be confirmed");
        }
        if (order != null && order.getPayTime() == null) {
            increaseSalesByOrderId(id);
        }
    }

    @Override
    @Transactional
    public void deliver(Long id) {
        int updated = wsOrderMapper.deliverOrder(id, LocalDateTime.now());
        if (updated == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Only pending shipment orders can be delivered");
        }
        decreaseStockByOrderId(id);
    }

    @Override
    public List<WsOrder> adminList(Integer status) {
        return wsOrderMapper.selectAll(status);
    }

    @Override
    public OrderDetailVO adminDetail(Long id) {
        WsOrder order = wsOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Order not found");
        }
        List<WsOrderItem> items = wsOrderItemMapper.selectByOrderId(order.getId());
        return new OrderDetailVO(order, items);
    }

    @Override
    public void refund(Long id) {
        int updated = wsOrderMapper.refundOrder(id);
        if (updated == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "Only paid or completed orders can be refunded");
        }
    }


    private void increaseSalesByOrderId(Long orderId) {
        List<WsOrderItem> items = wsOrderItemMapper.selectByOrderId(orderId);
        for (WsOrderItem item : items) {
            wsProductMapper.increaseSales(item.getProductId(), item.getQuantity());
        }
    }

    private void decreaseStockByOrderId(Long orderId) {
        List<WsOrderItem> items = wsOrderItemMapper.selectByOrderId(orderId);
        for (WsOrderItem item : items) {
            int updated = wsProductMapper.decreaseStock(item.getProductId(), item.getQuantity());
            if (updated == 0) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "Insufficient stock for product: " + item.getProductId());
            }
        }
    }

    private String generateOrderNo() {
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "WS" + timePart + rand;
    }
}
