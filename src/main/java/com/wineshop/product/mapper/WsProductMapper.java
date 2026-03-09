package com.wineshop.product.mapper;

import com.wineshop.product.entity.WsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsProductMapper {
    WsProduct selectById(@Param("id") Long id);

    List<WsProduct> selectAll();

    int insert(WsProduct product);

    int updateById(WsProduct product);

    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);

    int increaseSales(@Param("id") Long id, @Param("quantity") Integer quantity);

    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    int deleteById(@Param("id") Long id);
}
