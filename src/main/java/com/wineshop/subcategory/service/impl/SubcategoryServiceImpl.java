package com.wineshop.subcategory.service.impl;

import com.wineshop.subcategory.service.SubcategoryService;
import com.wineshop.subcategory.vo.SubcategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
    @Override
    public List<SubcategoryVO> listAll(Long categoryId) {
        // TODO: query ws_subcategory by mapper
        return List.of();
    }
}
