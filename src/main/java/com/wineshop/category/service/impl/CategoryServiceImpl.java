package com.wineshop.category.service.impl;

import com.wineshop.category.entity.WsCategory;
import com.wineshop.category.mapper.WsCategoryMapper;
import com.wineshop.category.service.CategoryService;
import com.wineshop.category.vo.CategoryTreeVO;
import com.wineshop.category.vo.CategoryVO;
import com.wineshop.subcategory.entity.WsSubcategory;
import com.wineshop.subcategory.mapper.WsSubcategoryMapper;
import com.wineshop.subcategory.vo.SubcategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final WsCategoryMapper wsCategoryMapper;
    private final WsSubcategoryMapper wsSubcategoryMapper;

    public CategoryServiceImpl(WsCategoryMapper wsCategoryMapper, WsSubcategoryMapper wsSubcategoryMapper) {
        this.wsCategoryMapper = wsCategoryMapper;
        this.wsSubcategoryMapper = wsSubcategoryMapper;
    }

    @Override
    public List<CategoryVO> listAll() {
        return wsCategoryMapper.selectAll().stream()
                .filter(c -> c.getStatus() != null && c.getStatus() == 1)
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryTreeVO> tree() {
        List<WsCategory> categories = wsCategoryMapper.selectAll().stream()
                .filter(c -> c.getStatus() != null && c.getStatus() == 1)
                .collect(Collectors.toList());
        List<WsSubcategory> subcategories = wsSubcategoryMapper.selectAll().stream()
                .filter(s -> s.getStatus() != null && s.getStatus() == 1)
                .collect(Collectors.toList());

        return categories.stream().map(category -> {
            CategoryTreeVO tree = new CategoryTreeVO();
            tree.setId(category.getId());
            tree.setName(category.getName());
            tree.setChildren(subcategories.stream()
                    .filter(s -> category.getId().equals(s.getCategoryId()))
                    .map(this::toSubcategoryVO)
                    .collect(Collectors.toList()));
            return tree;
        }).collect(Collectors.toList());
    }

    private CategoryVO toVO(WsCategory category) {
        CategoryVO vo = new CategoryVO();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setSort(category.getSort());
        return vo;
    }

    private SubcategoryVO toSubcategoryVO(WsSubcategory subcategory) {
        SubcategoryVO vo = new SubcategoryVO();
        vo.setId(subcategory.getId());
        vo.setCategoryId(subcategory.getCategoryId());
        vo.setName(subcategory.getName());
        vo.setSort(subcategory.getSort());
        return vo;
    }
}
