package com.wineshop.category.controller;

import com.wineshop.category.entity.WsCategory;
import com.wineshop.category.mapper.WsCategoryMapper;
import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final WsCategoryMapper wsCategoryMapper;

    public AdminCategoryController(WsCategoryMapper wsCategoryMapper) {
        this.wsCategoryMapper = wsCategoryMapper;
    }

    @GetMapping
    public Result<List<WsCategory>> list() {
        return Result.ok(wsCategoryMapper.selectAll());
    }

    @PostMapping
    public Result<Long> create(@RequestBody WsCategory category) {
        wsCategoryMapper.insert(category);
        return Result.ok(category.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsCategory category) {
        category.setId(id);
        if (wsCategoryMapper.updateById(category) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Category not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsCategoryMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Category not found");
        }
        return Result.ok();
    }
}
