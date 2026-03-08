package com.wineshop.favorite.service.impl;

import com.wineshop.common.security.AuthHelper;
import com.wineshop.favorite.entity.WsFavorite;
import com.wineshop.favorite.mapper.WsFavoriteMapper;
import com.wineshop.favorite.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final WsFavoriteMapper wsFavoriteMapper;

    public FavoriteServiceImpl(WsFavoriteMapper wsFavoriteMapper) {
        this.wsFavoriteMapper = wsFavoriteMapper;
    }

    @Override
    public List<WsFavorite> listMine() {
        Long userId = AuthHelper.currentUser().getId();
        return wsFavoriteMapper.selectByUserId(userId);
    }

    @Override
    public void add(Long productId) {
        Long userId = AuthHelper.currentUser().getId();
        WsFavorite existed = wsFavoriteMapper.selectByUserIdAndProductId(userId, productId);
        if (existed != null) {
            return;
        }
        WsFavorite favorite = new WsFavorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        wsFavoriteMapper.insert(favorite);
    }

    @Override
    public void delete(Long productId) {
        Long userId = AuthHelper.currentUser().getId();
        wsFavoriteMapper.deleteByUserIdAndProductId(userId, productId);
    }
}
