package com.wineshop.category.controller;

import com.wineshop.category.service.CategoryService;
import com.wineshop.category.vo.CategoryTreeVO;
import com.wineshop.category.vo.CategoryVO;
import com.wineshop.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<List<CategoryVO>> list() {
        return Result.ok(categoryService.listAll());
    }

    @GetMapping("/tree")
    public Result<List<CategoryTreeVO>> tree() {
        return Result.ok(categoryService.tree());
    }
}
