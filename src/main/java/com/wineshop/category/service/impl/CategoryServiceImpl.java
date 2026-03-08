package com.wineshop.category.service.impl;

import com.wineshop.category.service.CategoryService;
import com.wineshop.category.vo.CategoryTreeVO;
import com.wineshop.category.vo.CategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<CategoryVO> listAll() {
        // TODO: query ws_category by mapper
        return List.of();
    }

    @Override
    public List<CategoryTreeVO> tree() {
        // TODO: query ws_category + ws_subcategory and build tree
        return List.of();
    }
}
