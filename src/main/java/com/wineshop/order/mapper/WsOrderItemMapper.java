package com.wineshop.order.mapper;

import com.wineshop.order.entity.WsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsOrderItemMapper {
    int batchInsert(@Param("items") List<WsOrderItem> items);

    List<WsOrderItem> selectByOrderId(@Param("orderId") Long orderId);
}
