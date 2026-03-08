package com.wineshop.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {
        "com.wineshop.user.mapper",
        "com.wineshop.admin.mapper",
        "com.wineshop.category.mapper",
        "com.wineshop.subcategory.mapper",
        "com.wineshop.product.mapper",
        "com.wineshop.cart.mapper",
        "com.wineshop.favorite.mapper",
        "com.wineshop.address.mapper",
        "com.wineshop.order.mapper",
        "com.wineshop.comment.mapper",
        "com.wineshop.banner.mapper",
        "com.wineshop.notice.mapper",
        "com.wineshop.activity.mapper",
        "com.wineshop.statistics.mapper"
})
public class MybatisConfig {
}
