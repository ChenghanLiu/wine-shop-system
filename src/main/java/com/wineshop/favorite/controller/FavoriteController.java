package com.wineshop.favorite.controller;

import com.wineshop.common.result.Result;
import com.wineshop.favorite.entity.WsFavorite;
import com.wineshop.favorite.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public Result<List<WsFavorite>> list() {
        return Result.ok(favoriteService.listMine());
    }

    @PostMapping("/{productId}")
    public Result<Void> add(@PathVariable Long productId) {
        favoriteService.add(productId);
        return Result.ok();
    }

    @DeleteMapping("/{productId}")
    public Result<Void> delete(@PathVariable Long productId) {
        favoriteService.delete(productId);
        return Result.ok();
    }
}
