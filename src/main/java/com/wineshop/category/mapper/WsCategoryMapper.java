package com.wineshop.category.mapper;

import com.wineshop.category.entity.WsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsCategoryMapper {
    List<WsCategory> selectAll();

    int insert(WsCategory category);

    int updateById(WsCategory category);

    int deleteById(@Param("id") Long id);
}
