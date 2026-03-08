package com.wineshop.product.service.impl;

import com.wineshop.product.service.ProductService;
import com.wineshop.product.vo.ProductVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public List<ProductVO> list(String keyword, Long categoryId, Long subcategoryId) {
        // TODO: query ws_product by mapper with conditions
        return List.of();
    }

    @Override
    public ProductVO detail(Long id) {
        // TODO: query ws_product by id
        return null;
    }

    @Override
    public List<ProductVO> recommend() {
        // TODO: query ws_product where is_recommend = 1 and status = 1
        return List.of();
    }
}
