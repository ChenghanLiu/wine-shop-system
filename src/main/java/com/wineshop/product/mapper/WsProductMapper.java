package com.wineshop.product.mapper;

import com.wineshop.product.entity.WsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WsProductMapper {
    WsProduct selectById(@Param("id") Long id);

@Mapper
public interface WsProductMapper {
}
