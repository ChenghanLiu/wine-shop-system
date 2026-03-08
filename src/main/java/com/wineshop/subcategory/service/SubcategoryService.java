package com.wineshop.subcategory.service;

import com.wineshop.subcategory.vo.SubcategoryVO;

import java.util.List;

public interface SubcategoryService {
    List<SubcategoryVO> listAll(Long categoryId);
}
