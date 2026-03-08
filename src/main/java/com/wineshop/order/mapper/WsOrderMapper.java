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

@Mapper
public interface WsOrderMapper {
}
