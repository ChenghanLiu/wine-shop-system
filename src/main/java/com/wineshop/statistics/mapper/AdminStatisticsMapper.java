package com.wineshop.statistics.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminStatisticsMapper {
    Map<String, Object> selectOverview();

    List<Map<String, Object>> selectTopProducts();

    List<Map<String, Object>> selectCategoryRatio();
}
