package com.wineshop.product.service.impl;

import com.wineshop.common.exception.BusinessException;
import com.wineshop.common.result.ResultCode;
import com.wineshop.product.entity.WsProduct;
import com.wineshop.product.mapper.WsProductMapper;
import com.wineshop.product.service.ProductService;
import com.wineshop.product.vo.ProductVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final WsProductMapper wsProductMapper;

    public ProductServiceImpl(WsProductMapper wsProductMapper) {
        this.wsProductMapper = wsProductMapper;
    }

    @Override
    public List<ProductVO> list(String keyword, Long categoryId, Long subcategoryId) {
        return wsProductMapper.selectAll().stream()
                .filter(p -> p.getStatus() != null && p.getStatus() == 1)
                .filter(p -> keyword == null || keyword.isBlank() || (p.getName() != null && p.getName().contains(keyword)))
                .filter(p -> categoryId == null || categoryId.equals(p.getCategoryId()))
                .filter(p -> subcategoryId == null || subcategoryId.equals(p.getSubcategoryId()))
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductVO detail(Long id) {
        WsProduct product = wsProductMapper.selectById(id);
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            throw new BusinessException(ResultCode.NOT_FOUND, "Product not found");
        }
        return toVO(product);
    }

    @Override
    public List<ProductVO> recommend() {
        return wsProductMapper.selectAll().stream()
                .filter(p -> p.getStatus() != null && p.getStatus() == 1)
                .filter(p -> p.getIsRecommend() != null && p.getIsRecommend() == 1)
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    private ProductVO toVO(WsProduct product) {
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setCategoryId(product.getCategoryId());
        vo.setSubcategoryId(product.getSubcategoryId());
        vo.setCoverImage(product.getCoverImage());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setSales(product.getSales());
        vo.setDescription(product.getDescription());
        vo.setIsRecommend(product.getIsRecommend());
        return vo;
    }
}
