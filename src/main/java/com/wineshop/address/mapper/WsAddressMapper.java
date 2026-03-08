package com.wineshop.address.mapper;

import com.wineshop.address.entity.WsAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsAddressMapper {
    List<WsAddress> selectByUserId(@Param("userId") Long userId);

    WsAddress selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int insert(WsAddress address);

    int updateByIdAndUserId(WsAddress address);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    int clearDefaultByUserId(@Param("userId") Long userId);

    int setDefaultByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
