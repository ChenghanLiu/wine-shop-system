-- Wine Shop System - data_init.sql
-- Execute after schema.sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1) Default admin account
-- ----------------------------
-- Default account:
-- username: admin
-- password: admin123  (demo environment only; replace in production)
INSERT INTO ws_admin (id, username, password, real_name, status, created_at, updated_at)
VALUES (1, 'admin', 'admin123', '系统管理员', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  password = VALUES(password),
  real_name = VALUES(real_name),
  status = VALUES(status),
  updated_at = NOW();

-- ----------------------------
-- 2) Demo normal user
-- ----------------------------
INSERT INTO ws_user (id, username, password, nickname, phone, status, created_at, updated_at)
VALUES (10001, 'user1', '123456', '演示用户', '13800000001', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  password = VALUES(password),
  nickname = VALUES(nickname),
  phone = VALUES(phone),
  status = VALUES(status),
  updated_at = NOW();

-- ----------------------------
-- 3) Fixed primary categories
-- ----------------------------
INSERT INTO ws_category (id, name, sort, status) VALUES
(1, '白酒', 1, 1),
(2, '啤酒', 2, 1),
(3, '红酒', 3, 1),
(4, '洋酒', 4, 1)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  sort = VALUES(sort),
  status = VALUES(status);

-- ----------------------------
-- 4) Fixed secondary categories
-- ----------------------------
INSERT INTO ws_subcategory (id, category_id, name, sort, status) VALUES
-- 白酒
(101, 1, '酱香', 1, 1),
(102, 1, '浓香', 2, 1),
(103, 1, '清香', 3, 1),
(104, 1, '兼香', 4, 1),
(105, 1, '其他香型', 5, 1),
-- 啤酒
(201, 2, '工业啤酒', 1, 1),
(202, 2, '精酿啤酒', 2, 1),
(203, 2, '进口啤酒', 3, 1),
(204, 2, '果味啤酒', 4, 1),
-- 红酒
(301, 3, '干红', 1, 1),
(302, 3, '甜红', 2, 1),
(303, 3, '起泡酒', 3, 1),
(304, 3, '进口红酒', 4, 1),
(305, 3, '国产红酒', 5, 1),
-- 洋酒
(401, 4, '威士忌', 1, 1),
(402, 4, '白兰地', 2, 1),
(403, 4, '伏特加', 3, 1),
(404, 4, '朗姆酒', 4, 1),
(405, 4, '金酒', 5, 1)
ON DUPLICATE KEY UPDATE
  category_id = VALUES(category_id),
  name = VALUES(name),
  sort = VALUES(sort),
  status = VALUES(status);

-- ----------------------------
-- 5) Demo products
-- ----------------------------
INSERT INTO ws_product
(id, name, category_id, subcategory_id, cover_image, price, stock, sales, description, status, is_recommend)
VALUES
(1001, '贵州酱香白酒 500ml', 1, 101, 'https://demo.wineshop.com/images/p1001.jpg', 199.00, 120, 56,
 '酱香突出、口感醇厚，适合聚会与礼赠。', 1, 1),
(1002, '经典浓香白酒 52度', 1, 102, 'https://demo.wineshop.com/images/p1002.jpg', 139.00, 200, 88,
 '浓香风味明显，入口绵甜，回味悠长。', 1, 1),
(1003, '清爽工业啤酒 330ml*24听', 2, 201, 'https://demo.wineshop.com/images/p1003.jpg', 96.00, 300, 150,
 '经典工业拉格口味，适合日常畅饮。', 1, 0),
(1004, '精酿IPA啤酒 500ml*12瓶', 2, 202, 'https://demo.wineshop.com/images/p1004.jpg', 168.00, 90, 43,
 '酒花香气浓郁，苦度平衡，精酿爱好者推荐。', 1, 1),
(1005, '进口干红葡萄酒 750ml', 3, 304, 'https://demo.wineshop.com/images/p1005.jpg', 228.00, 80, 39,
 '果香浓郁，单宁柔顺，适合牛排与烤肉搭配。', 1, 1),
(1006, '国产甜红葡萄酒 750ml', 3, 302, 'https://demo.wineshop.com/images/p1006.jpg', 88.00, 140, 61,
 '口感柔和偏甜，适合入门用户。', 1, 0),
(1007, '苏格兰威士忌 700ml', 4, 401, 'https://demo.wineshop.com/images/p1007.jpg', 299.00, 70, 27,
 '经典橡木桶陈酿风味，层次丰富。', 1, 1),
(1008, '经典白兰地 700ml', 4, 402, 'https://demo.wineshop.com/images/p1008.jpg', 259.00, 65, 21,
 '果香与木香平衡，适合作为餐后酒。', 1, 0)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  category_id = VALUES(category_id),
  subcategory_id = VALUES(subcategory_id),
  cover_image = VALUES(cover_image),
  price = VALUES(price),
  stock = VALUES(stock),
  sales = VALUES(sales),
  description = VALUES(description),
  status = VALUES(status),
  is_recommend = VALUES(is_recommend);

-- Demo product detail images
DELETE FROM ws_product_image WHERE product_id IN (1001, 1004, 1005);
INSERT INTO ws_product_image (product_id, image_url, sort) VALUES
(1001, 'https://demo.wineshop.com/images/p1001_detail_1.jpg', 1),
(1001, 'https://demo.wineshop.com/images/p1001_detail_2.jpg', 2),
(1004, 'https://demo.wineshop.com/images/p1004_detail_1.jpg', 1),
(1005, 'https://demo.wineshop.com/images/p1005_detail_1.jpg', 1);

-- ----------------------------
-- 6) Demo addresses for user1
-- ----------------------------
DELETE FROM ws_address WHERE user_id = 10001;
INSERT INTO ws_address
(id, user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default, created_at, updated_at)
VALUES
(20001, 10001, '张三', '13800000001', '广东省', '深圳市', '南山区', '科技园科苑路1号', 1, NOW(), NOW()),
(20002, 10001, '张三', '13800000001', '广东省', '广州市', '天河区', '珠江新城花城大道88号', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  receiver_name = VALUES(receiver_name),
  receiver_phone = VALUES(receiver_phone),
  province = VALUES(province),
  city = VALUES(city),
  district = VALUES(district),
  detail_address = VALUES(detail_address),
  is_default = VALUES(is_default),
  updated_at = NOW();

-- ----------------------------
-- 7) Demo cart and favorites for user1
-- ----------------------------
DELETE FROM ws_cart WHERE user_id = 10001;
INSERT INTO ws_cart (id, user_id, product_id, quantity, selected, created_at, updated_at) VALUES
(30001, 10001, 1001, 2, 1, NOW(), NOW()),
(30002, 10001, 1003, 1, 1, NOW(), NOW()),
(30003, 10001, 1007, 1, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  quantity = VALUES(quantity),
  selected = VALUES(selected),
  updated_at = NOW();

DELETE FROM ws_favorite WHERE user_id = 10001;
INSERT INTO ws_favorite (id, user_id, product_id, created_at) VALUES
(40001, 10001, 1004, NOW()),
(40002, 10001, 1005, NOW())
ON DUPLICATE KEY UPDATE
  created_at = VALUES(created_at);

-- ----------------------------
-- 8) Demo banners
-- ----------------------------
INSERT INTO ws_banner (id, title, image_url, link_url, sort, status) VALUES
(1, '新品精酿节', 'https://demo.wineshop.com/banner/banner1.jpg', '/products?categoryId=2', 1, 1),
(2, '红酒品鉴月', 'https://demo.wineshop.com/banner/banner2.jpg', '/products?categoryId=3', 2, 1),
(3, '洋酒礼盒专区', 'https://demo.wineshop.com/banner/banner3.jpg', '/products?categoryId=4', 3, 1)
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  image_url = VALUES(image_url),
  link_url = VALUES(link_url),
  sort = VALUES(sort),
  status = VALUES(status);

-- ----------------------------
-- 9) Demo notices
-- ----------------------------
INSERT INTO ws_notice (id, title, content, type, status, publish_time) VALUES
(1, '系统上线公告', '酒水销售系统演示版已正式上线，欢迎体验选购。', 1, 1, NOW()),
(2, '配送时效说明', '演示环境订单默认24小时内发货，具体以后台操作为准。', 1, 1, NOW()),
(3, '红酒基础知识', '入门可优先选择甜红或果香明显的干红，口感更友好。', 2, 1, NOW())
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  content = VALUES(content),
  type = VALUES(type),
  status = VALUES(status),
  publish_time = VALUES(publish_time);

-- ----------------------------
-- 10) Demo activities
-- ----------------------------
INSERT INTO ws_activity (id, title, cover_image, content, start_time, end_time, status) VALUES
(1, '618 酒水狂欢节', 'https://demo.wineshop.com/activity/a1.jpg',
 '活动期间指定商品满199减20，满399减50（演示规则）。',
 '2026-06-01 00:00:00', '2026-06-20 23:59:59', 1),
(2, '夏日精酿周', 'https://demo.wineshop.com/activity/a2.jpg',
 '精选精酿啤酒限时折扣，支持首页活动专区展示。',
 '2026-07-01 00:00:00', '2026-07-07 23:59:59', 1),
(3, '中秋礼赠洋酒季', 'https://demo.wineshop.com/activity/a3.jpg',
 '洋酒礼盒专题活动，适合毕业设计演示运营场景。',
 '2026-09-01 00:00:00', '2026-09-18 23:59:59', 1)
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  cover_image = VALUES(cover_image),
  content = VALUES(content),
  start_time = VALUES(start_time),
  end_time = VALUES(end_time),
  status = VALUES(status);

-- Optional extension: activity join demo record
DELETE FROM ws_activity_join WHERE user_id = 10001;
INSERT INTO ws_activity_join (id, activity_id, user_id, created_at)
VALUES (50001, 1, 10001, NOW())
ON DUPLICATE KEY UPDATE
  activity_id = VALUES(activity_id),
  created_at = VALUES(created_at);

-- ----------------------------
-- 11) Demo orders / items / comment
-- ----------------------------
DELETE FROM ws_comment WHERE user_id = 10001;
DELETE FROM ws_order_item WHERE order_id IN (60001, 60002);
DELETE FROM ws_order WHERE id IN (60001, 60002);

-- one pending payment order
INSERT INTO ws_order
(id, order_no, user_id, total_amount, pay_amount, status,
 receiver_name, receiver_phone, province, city, district, detail_address,
 remark, pay_time, delivery_time, receive_time, created_at, updated_at)
VALUES
(60001, 'WS202603080001', 10001, 463.00, 0.00, 0,
 '张三', '13800000001', '广东省', '深圳市', '南山区', '科技园科苑路1号',
 '演示待支付订单', NULL, NULL, NULL, NOW(), NOW()),
-- one completed order
(60002, 'WS202603080002', 10001, 228.00, 228.00, 3,
 '张三', '13800000001', '广东省', '广州市', '天河区', '珠江新城花城大道88号',
 '演示已完成订单', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NOW())
ON DUPLICATE KEY UPDATE
  total_amount = VALUES(total_amount),
  pay_amount = VALUES(pay_amount),
  status = VALUES(status),
  receiver_name = VALUES(receiver_name),
  receiver_phone = VALUES(receiver_phone),
  province = VALUES(province),
  city = VALUES(city),
  district = VALUES(district),
  detail_address = VALUES(detail_address),
  remark = VALUES(remark),
  pay_time = VALUES(pay_time),
  delivery_time = VALUES(delivery_time),
  receive_time = VALUES(receive_time),
  updated_at = NOW();

INSERT INTO ws_order_item
(id, order_id, order_no, product_id, product_name, product_cover, product_price, quantity, total_price, created_at)
VALUES
(70001, 60001, 'WS202603080001', 1001, '贵州酱香白酒 500ml', 'https://demo.wineshop.com/images/p1001.jpg', 199.00, 1, 199.00, NOW()),
(70002, 60001, 'WS202603080001', 1004, '精酿IPA啤酒 500ml*12瓶', 'https://demo.wineshop.com/images/p1004.jpg', 168.00, 1, 168.00, NOW()),
(70003, 60001, 'WS202603080001', 1003, '清爽工业啤酒 330ml*24听', 'https://demo.wineshop.com/images/p1003.jpg', 96.00, 1, 96.00, NOW()),
(70004, 60002, 'WS202603080002', 1005, '进口干红葡萄酒 750ml', 'https://demo.wineshop.com/images/p1005.jpg', 228.00, 1, 228.00, NOW())
ON DUPLICATE KEY UPDATE
  product_name = VALUES(product_name),
  product_cover = VALUES(product_cover),
  product_price = VALUES(product_price),
  quantity = VALUES(quantity),
  total_price = VALUES(total_price);

-- completed order comment (one order item can only be commented once)
INSERT INTO ws_comment
(id, order_id, order_item_id, user_id, product_id, score, content, status, created_at, updated_at)
VALUES
(80001, 60002, 70004, 10001, 1005, 5, '口感顺滑，果香明显，适合聚餐搭配。', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  score = VALUES(score),
  content = VALUES(content),
  status = VALUES(status),
  updated_at = NOW();

SET FOREIGN_KEY_CHECKS = 1;
