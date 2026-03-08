package com.wineshop.favorite.service;

import com.wineshop.favorite.entity.WsFavorite;

import java.util.List;

public interface FavoriteService {
    List<WsFavorite> listMine();

    void add(Long productId);

    void delete(Long productId);
}
