package com.wineshop.banner.controller;

import com.wineshop.banner.entity.WsBanner;
import com.wineshop.banner.mapper.WsBannerMapper;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/banners")
public class AdminBannerController {

    private final WsBannerMapper wsBannerMapper;

    public AdminBannerController(WsBannerMapper wsBannerMapper) {
        this.wsBannerMapper = wsBannerMapper;
    }

    @GetMapping
    public Result<List<WsBanner>> list() {
        return Result.ok(wsBannerMapper.selectAll());
    }

    @PostMapping
    public Result<Long> create(@RequestBody WsBanner banner) {
        wsBannerMapper.insert(banner);
        return Result.ok(banner.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsBanner banner) {
        banner.setId(id);
        if (wsBannerMapper.updateById(banner) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Banner not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsBannerMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Banner not found");
        }
        return Result.ok();
    }
}
