package com.wineshop.product.controller;

import com.wineshop.common.result.Result;
import com.wineshop.product.service.ProductService;
import com.wineshop.product.vo.ProductVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<List<ProductVO>> list(@RequestParam(required = false) String keyword,
                                        @RequestParam(required = false) Long categoryId,
                                        @RequestParam(required = false) Long subcategoryId) {
        return Result.ok(productService.list(keyword, categoryId, subcategoryId));
    }

    @GetMapping("/{id}")
    public Result<ProductVO> detail(@PathVariable Long id) {
        return Result.ok(productService.detail(id));
    }

    @GetMapping("/recommend")
    public Result<List<ProductVO>> recommend() {
        return Result.ok(productService.recommend());
    }
}
