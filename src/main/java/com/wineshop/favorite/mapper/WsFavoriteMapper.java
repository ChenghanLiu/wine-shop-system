package com.wineshop.favorite.mapper;

import com.wineshop.favorite.entity.WsFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WsFavoriteMapper {
    List<WsFavorite> selectByUserId(@Param("userId") Long userId);

    WsFavorite selectByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    int insert(WsFavorite favorite);

    int deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
