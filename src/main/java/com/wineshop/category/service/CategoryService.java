package com.wineshop.category.service;

import com.wineshop.category.vo.CategoryTreeVO;
import com.wineshop.category.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    List<CategoryVO> listAll();

    List<CategoryTreeVO> tree();
}
