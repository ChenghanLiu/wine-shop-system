package com.wineshop.order.mapper;

import com.wineshop.order.entity.WsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WsOrderMapper {
    int insert(WsOrder order);

    List<WsOrder> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    WsOrder selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int payOrder(@Param("id") Long id, @Param("userId") Long userId, @Param("payTime") LocalDateTime payTime);

    int cancelOrder(@Param("id") Long id, @Param("userId") Long userId);

    int confirmOrder(@Param("id") Long id, @Param("userId") Long userId, @Param("receiveTime") LocalDateTime receiveTime);

    int deliverOrder(@Param("id") Long id, @Param("deliveryTime") LocalDateTime deliveryTime);

    List<WsOrder> selectAll(@Param("status") Integer status);

    WsOrder selectById(@Param("id") Long id);

    int refundOrder(@Param("id") Long id);
}
