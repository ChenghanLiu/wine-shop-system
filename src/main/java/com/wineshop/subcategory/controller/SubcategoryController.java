package com.wineshop.subcategory.controller;

import com.wineshop.common.result.Result;
import com.wineshop.subcategory.service.SubcategoryService;
import com.wineshop.subcategory.vo.SubcategoryVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    public SubcategoryController(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    @GetMapping
    public Result<List<SubcategoryVO>> list(@RequestParam(required = false) Long categoryId) {
        return Result.ok(subcategoryService.listAll(categoryId));
    }
}
