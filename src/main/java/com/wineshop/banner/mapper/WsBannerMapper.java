package com.wineshop.banner.mapper;

import com.wineshop.banner.entity.WsBanner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsBannerMapper {
    List<WsBanner> selectAll();

    int insert(WsBanner banner);

    int updateById(WsBanner banner);

    int deleteById(@Param("id") Long id);
}
