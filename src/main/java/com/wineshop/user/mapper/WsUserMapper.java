package com.wineshop.user.mapper;

import com.wineshop.user.entity.WsUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WsUserMapper {
    WsUser selectByUsername(@Param("username") String username);

    WsUser selectById(@Param("id") Long id);

    int insert(WsUser user);
}
