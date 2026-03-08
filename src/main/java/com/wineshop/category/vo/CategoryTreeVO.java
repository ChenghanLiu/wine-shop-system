package com.wineshop.category.vo;

import com.wineshop.subcategory.vo.SubcategoryVO;
import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeVO {
    private Long id;
    private String name;
    private List<SubcategoryVO> children;
}
