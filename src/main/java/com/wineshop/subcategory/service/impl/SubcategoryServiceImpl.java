package com.wineshop.subcategory.service.impl;

import com.wineshop.subcategory.entity.WsSubcategory;
import com.wineshop.subcategory.mapper.WsSubcategoryMapper;
import com.wineshop.subcategory.service.SubcategoryService;
import com.wineshop.subcategory.vo.SubcategoryVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    private final WsSubcategoryMapper wsSubcategoryMapper;

    public SubcategoryServiceImpl(WsSubcategoryMapper wsSubcategoryMapper) {
        this.wsSubcategoryMapper = wsSubcategoryMapper;
    }

    @Override
    public List<SubcategoryVO> listAll(Long categoryId) {
        return wsSubcategoryMapper.selectAll().stream()
                .filter(s -> s.getStatus() != null && s.getStatus() == 1)
                .filter(s -> categoryId == null || categoryId.equals(s.getCategoryId()))
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    private SubcategoryVO toVO(WsSubcategory subcategory) {
        SubcategoryVO vo = new SubcategoryVO();
        vo.setId(subcategory.getId());
        vo.setCategoryId(subcategory.getCategoryId());
        vo.setName(subcategory.getName());
        vo.setSort(subcategory.getSort());
        return vo;
    }
}
