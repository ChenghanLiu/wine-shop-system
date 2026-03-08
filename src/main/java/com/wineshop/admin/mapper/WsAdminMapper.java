package com.wineshop.admin.mapper;

import com.wineshop.admin.entity.WsAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WsAdminMapper {
    WsAdmin selectByUsername(@Param("username") String username);

    WsAdmin selectById(@Param("id") Long id);
}