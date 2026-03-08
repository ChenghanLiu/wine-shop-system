-- Wine Shop System - schema.sql
-- MySQL 8.0+
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Drop tables (dependency order)
-- ----------------------------
DROP TABLE IF EXISTS ws_activity_join;
DROP TABLE IF EXISTS ws_comment;
DROP TABLE IF EXISTS ws_order_item;
DROP TABLE IF EXISTS ws_order;
DROP TABLE IF EXISTS ws_cart;
DROP TABLE IF EXISTS ws_favorite;
DROP TABLE IF EXISTS ws_address;
DROP TABLE IF EXISTS ws_product_image;
DROP TABLE IF EXISTS ws_product;
DROP TABLE IF EXISTS ws_subcategory;
DROP TABLE IF EXISTS ws_category;
DROP TABLE IF EXISTS ws_banner;
DROP TABLE IF EXISTS ws_notice;
DROP TABLE IF EXISTS ws_activity;
DROP TABLE IF EXISTS ws_user;
DROP TABLE IF EXISTS ws_admin;

-- ----------------------------
-- Account tables
-- ----------------------------
CREATE TABLE ws_user (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT '密码(建议加密存储)',
  nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1正常,0禁用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_user_username (username),
  UNIQUE KEY uk_ws_user_phone (phone),
  KEY idx_ws_user_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE ws_admin (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  username VARCHAR(50) NOT NULL COMMENT '管理员账号',
  password VARCHAR(100) NOT NULL COMMENT '密码(建议加密存储)',
  real_name VARCHAR(50) DEFAULT NULL COMMENT '姓名',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1正常,0禁用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_admin_username (username),
  KEY idx_ws_admin_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';

-- ----------------------------
-- Category and product tables
-- ----------------------------
CREATE TABLE ws_category (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '一级分类ID',
  name VARCHAR(50) NOT NULL COMMENT '一级分类名称',
  sort INT NOT NULL DEFAULT 0 COMMENT '排序值',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用,0禁用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_category_name (name),
  KEY idx_ws_category_status_sort (status, sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='一级分类表';

CREATE TABLE ws_subcategory (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '二级分类ID',
  category_id BIGINT UNSIGNED NOT NULL COMMENT '一级分类ID',
  name VARCHAR(50) NOT NULL COMMENT '二级分类名称',
  sort INT NOT NULL DEFAULT 0 COMMENT '排序值',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用,0禁用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_subcategory_category_name (category_id, name),
  KEY idx_ws_subcategory_status_sort (status, sort),
  CONSTRAINT fk_ws_subcategory_category FOREIGN KEY (category_id) REFERENCES ws_category (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='二级分类表';

CREATE TABLE ws_product (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  name VARCHAR(120) NOT NULL COMMENT '商品名称',
  category_id BIGINT UNSIGNED NOT NULL COMMENT '一级分类ID',
  subcategory_id BIGINT UNSIGNED NOT NULL COMMENT '二级分类ID',
  cover_image VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
  price DECIMAL(10,2) NOT NULL COMMENT '售价',
  stock INT NOT NULL DEFAULT 0 COMMENT '库存',
  sales INT NOT NULL DEFAULT 0 COMMENT '销量',
  description TEXT COMMENT '商品描述',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1上架,0下架',
  is_recommend TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐:1是,0否',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_ws_product_category (category_id),
  KEY idx_ws_product_subcategory (subcategory_id),
  KEY idx_ws_product_status_recommend (status, is_recommend),
  KEY idx_ws_product_price (price),
  KEY idx_ws_product_status_category_subcategory (status, category_id, subcategory_id),
  KEY idx_ws_product_name (name),
  CONSTRAINT fk_ws_product_category FOREIGN KEY (category_id) REFERENCES ws_category (id),
  CONSTRAINT fk_ws_product_subcategory FOREIGN KEY (subcategory_id) REFERENCES ws_subcategory (id),
  CONSTRAINT chk_ws_product_price_nonnegative CHECK (price >= 0),
  CONSTRAINT chk_ws_product_stock_nonnegative CHECK (stock >= 0),
  CONSTRAINT chk_ws_product_sales_nonnegative CHECK (sales >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

CREATE TABLE ws_product_image (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  image_url VARCHAR(255) NOT NULL COMMENT '详情图URL',
  sort INT NOT NULL DEFAULT 0 COMMENT '排序值',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_ws_product_image_product_sort (product_id, sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品详情图表';

-- ----------------------------
-- Cart / favorite / address
-- ----------------------------
CREATE TABLE ws_cart (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
  selected TINYINT NOT NULL DEFAULT 1 COMMENT '是否勾选:1是,0否',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_cart_user_product (user_id, product_id),
  KEY idx_ws_cart_user (user_id),
  KEY idx_ws_cart_product (product_id),
  CONSTRAINT chk_ws_cart_quantity_positive CHECK (quantity > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';

CREATE TABLE ws_favorite (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_favorite_user_product (user_id, product_id),
  KEY idx_ws_favorite_user (user_id),
  KEY idx_ws_favorite_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';

CREATE TABLE ws_address (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  receiver_name VARCHAR(50) NOT NULL COMMENT '收货人',
  receiver_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
  province VARCHAR(50) NOT NULL COMMENT '省',
  city VARCHAR(50) NOT NULL COMMENT '市',
  district VARCHAR(50) NOT NULL COMMENT '区/县',
  detail_address VARCHAR(255) NOT NULL COMMENT '详细地址',
  is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认:1是,0否',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_ws_address_user (user_id),
  KEY idx_ws_address_user_default (user_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收货地址表';

-- ----------------------------
-- Order / order item / comment
-- ----------------------------
CREATE TABLE ws_order (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  order_no VARCHAR(32) NOT NULL COMMENT '订单号',
  user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
  pay_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '0待支付 1待发货 2待收货 3已完成 4已取消 5已退款',
  receiver_name VARCHAR(50) NOT NULL COMMENT '收货人',
  receiver_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
  province VARCHAR(50) NOT NULL COMMENT '省',
  city VARCHAR(50) NOT NULL COMMENT '市',
  district VARCHAR(50) NOT NULL COMMENT '区/县',
  detail_address VARCHAR(255) NOT NULL COMMENT '详细地址',
  remark VARCHAR(255) DEFAULT NULL COMMENT '用户备注',
  pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
  delivery_time DATETIME DEFAULT NULL COMMENT '发货时间',
  receive_time DATETIME DEFAULT NULL COMMENT '收货时间',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_order_order_no (order_no),
  KEY idx_ws_order_user (user_id),
  KEY idx_ws_order_status (status),
  KEY idx_ws_order_created_at (created_at),
  KEY idx_ws_order_user_status (user_id, status),
  KEY idx_ws_order_status_created_at (status, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单主表';

CREATE TABLE ws_order_item (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  order_no VARCHAR(32) NOT NULL COMMENT '订单号',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  product_name VARCHAR(120) NOT NULL COMMENT '商品名快照',
  product_cover VARCHAR(255) DEFAULT NULL COMMENT '商品图快照',
  product_price DECIMAL(10,2) NOT NULL COMMENT '商品单价快照',
  quantity INT NOT NULL COMMENT '购买数量',
  total_price DECIMAL(10,2) NOT NULL COMMENT '该项总价',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_ws_order_item_order (order_id),
  KEY idx_ws_order_item_order_no (order_no),
  KEY idx_ws_order_item_product (product_id),
  CONSTRAINT fk_ws_order_item_order FOREIGN KEY (order_id) REFERENCES ws_order (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';

CREATE TABLE ws_comment (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  order_id BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  order_item_id BIGINT UNSIGNED NOT NULL COMMENT '订单项ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  product_id BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  score TINYINT NOT NULL COMMENT '评分(1-5)',
  content VARCHAR(500) NOT NULL COMMENT '评论内容',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1显示,0隐藏',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_comment_order_item (order_item_id),
  KEY idx_ws_comment_order (order_id),
  KEY idx_ws_comment_product (product_id),
  KEY idx_ws_comment_user (user_id),
  KEY idx_ws_comment_status_created (status, created_at),
  CONSTRAINT chk_ws_comment_score_range CHECK (score >= 1 AND score <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表(订单项一对一评论, score范围1~5)';

-- ----------------------------
-- Operation content tables
-- ----------------------------
CREATE TABLE ws_banner (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
  title VARCHAR(100) NOT NULL COMMENT '标题',
  image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
  link_url VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
  sort INT NOT NULL DEFAULT 0 COMMENT '排序值',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1启用,0停用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_ws_banner_status_sort (status, sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='轮播图表';

CREATE TABLE ws_notice (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  title VARCHAR(150) NOT NULL COMMENT '标题',
  content TEXT NOT NULL COMMENT '内容',
  type TINYINT NOT NULL DEFAULT 1 COMMENT '类型:1公告,2新闻',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1发布,0下线',
  publish_time DATETIME DEFAULT NULL COMMENT '发布时间',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_ws_notice_type_status_publish (type, status, publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告新闻表';

CREATE TABLE ws_activity (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  title VARCHAR(100) NOT NULL COMMENT '活动标题',
  cover_image VARCHAR(255) DEFAULT NULL COMMENT '封面图URL',
  content TEXT NOT NULL COMMENT '活动内容',
  start_time DATETIME NOT NULL COMMENT '开始时间',
  end_time DATETIME NOT NULL COMMENT '结束时间',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1发布,0下线',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_ws_activity_status_time (status, start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动表';

CREATE TABLE ws_activity_join (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '活动参与记录ID',
  activity_id BIGINT UNSIGNED NOT NULL COMMENT '活动ID',
  user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_ws_activity_join_activity_user (activity_id, user_id),
  KEY idx_ws_activity_join_activity (activity_id),
  KEY idx_ws_activity_join_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动参与表(可选扩展)';

SET FOREIGN_KEY_CHECKS = 1;
