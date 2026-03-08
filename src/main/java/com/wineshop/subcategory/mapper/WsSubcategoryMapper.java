package com.wineshop.subcategory.mapper;

import com.wineshop.subcategory.entity.WsSubcategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsSubcategoryMapper {
    List<WsSubcategory> selectAll();

    int insert(WsSubcategory subcategory);

    int updateById(WsSubcategory subcategory);

    int deleteById(@Param("id") Long id);
}
