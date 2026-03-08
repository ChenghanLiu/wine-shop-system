package com.wineshop.cart.mapper;

import com.wineshop.cart.entity.WsCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsCartMapper {
    List<WsCart> selectByUserId(@Param("userId") Long userId);

    List<WsCart> selectSelectedByUserId(@Param("userId") Long userId);

    WsCart selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    WsCart selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int insert(WsCart cart);

    int updateByIdAndUserId(WsCart cart);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int deleteSelectedByUserId(@Param("userId") Long userId);
}
