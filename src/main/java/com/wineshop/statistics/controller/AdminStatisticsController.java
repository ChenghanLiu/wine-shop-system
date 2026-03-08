package com.wineshop.statistics.controller;

import com.wineshop.common.result.Result;
import com.wineshop.statistics.mapper.AdminStatisticsMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsController {

    private final AdminStatisticsMapper adminStatisticsMapper;

    public AdminStatisticsController(AdminStatisticsMapper adminStatisticsMapper) {
        this.adminStatisticsMapper = adminStatisticsMapper;
    }

    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        return Result.ok(adminStatisticsMapper.selectOverview());
    }

    @GetMapping("/top-products")
    public Result<List<Map<String, Object>>> topProducts() {
        return Result.ok(adminStatisticsMapper.selectTopProducts());
    }

    @GetMapping("/category-ratio")
    public Result<List<Map<String, Object>>> categoryRatio() {
        return Result.ok(adminStatisticsMapper.selectCategoryRatio());
    }
}
