package com.wineshop.activity.mapper;

import com.wineshop.activity.entity.WsActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsActivityMapper {
    List<WsActivity> selectAll();

    int insert(WsActivity activity);

    int updateById(WsActivity activity);

    int deleteById(@Param("id") Long id);
}
