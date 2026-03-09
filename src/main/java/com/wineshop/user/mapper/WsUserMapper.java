package com.wineshop.user.mapper;

import com.wineshop.user.entity.WsUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsUserMapper {
    WsUser selectByUsername(@Param("username") String username);

    WsUser selectById(@Param("id") Long id);

    List<WsUser> selectAll();

    int insert(WsUser user);

    int updateProfileById(WsUser user);

    int updateByAdmin(WsUser user);

    int deleteById(@Param("id") Long id);
}
