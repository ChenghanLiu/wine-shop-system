package com.wineshop.product.controller;

import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.Result;
import com.wineshop.common.result.ResultCode;
import com.wineshop.product.entity.WsProduct;
import com.wineshop.product.mapper.WsProductMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final WsProductMapper wsProductMapper;

    public AdminProductController(WsProductMapper wsProductMapper) {
        this.wsProductMapper = wsProductMapper;
    }

    @GetMapping
    public Result<List<WsProduct>> list() {
        return Result.ok(wsProductMapper.selectAll());
    }

    @PostMapping
    public Result<Long> create(@RequestBody WsProduct product) {
        wsProductMapper.insert(product);
        return Result.ok(product.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody WsProduct product) {
        product.setId(id);
        if (wsProductMapper.updateById(product) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Product not found");
        }
        return Result.ok();
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (wsProductMapper.updateStatusById(id, status) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Product not found");
        }
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (wsProductMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Product not found");
        }
        return Result.ok();
    }
}
