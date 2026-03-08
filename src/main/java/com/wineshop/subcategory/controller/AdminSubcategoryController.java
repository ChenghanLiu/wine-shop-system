package com.wineshop.subcategory.controller;

import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import com.wineshop.subcategory.entity.WsSubcategory;
import com.wineshop.subcategory.mapper.WsSubcategoryMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/subcategories")
public class AdminSubcategoryController {

    private final WsSubcategoryMapper wsSubcategoryMapper;

    public AdminSubcategoryController(WsSubcategoryMapper wsSubcategoryMapper) {
        this.wsSubcategoryMapper = wsSubcategoryMapper;
    }

    @GetMapping
    public Result<List<WsSubcategory>> list() {
        return Result.ok(wsSubcategoryMapper.selectAll());
    }

    @PostMapping
    public Result<Long> create(@RequestBody WsSubcategory subcategory) {
        wsSubcategoryMapper.insert(subcategory);
        return Result.ok(subcategory.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsSubcategory subcategory) {
        subcategory.setId(id);
        if (wsSubcategoryMapper.updateById(subcategory) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Subcategory not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsSubcategoryMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Subcategory not found");
        }
        return Result.ok();
    }
}
