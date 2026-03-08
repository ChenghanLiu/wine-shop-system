package com.wineshop.product.service;

import com.wineshop.product.vo.ProductVO;

import java.util.List;

public interface ProductService {
    List<ProductVO> list(String keyword, Long categoryId, Long subcategoryId);

    ProductVO detail(Long id);

    List<ProductVO> recommend();
}
