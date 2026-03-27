-- NOTE:
-- This seed is designed as "official sample data" for the new requirement.
-- It assumes a fresh DB for classroom/demo usage.

-- ===============================
-- I. Main Categories (Roman numerals)
-- ===============================
INSERT INTO categories (name, description) VALUES
('I. Electronics, electrical appliances', 'Main category I'),
('II. Househould appliances', 'Main category II'),
('III. Electronics, telecommunications', 'Main category III'),
('IV. Beauty, personal care', 'Main category IV'),
('V. Accessories', 'Main category V'),
('VI. Other products', 'Main category VI');

-- ===============================
-- SubCategories for each Main Category
-- ===============================
-- I
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '1. Air conditional', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '2. Dishwasher', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '3. Micro', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '4. Refrigerator', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '5. Speaker', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '6. Tivi', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '7. Washing machine', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';
INSERT INTO sub_categories (category_id, name, image_url)
SELECT c.id, '8. Water heater', NULL FROM categories c WHERE c.name='I. Electronics, electrical appliances';

-- II
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '1. Air fryer', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '2. Air purifier', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '3. Blender', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '4. Cooking pot', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '5. Cutting board', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '6. Electric cooker', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '7. Electric kettle', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '8. Fan', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '9. Gas stove', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '10. Hair dryer', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '11. Induction cooktop', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '12. Infrared cooker', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '13. Iron', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '14. Knife', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '15. Microwave oven', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '16. Mop set', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '17. Mosquito racket', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '18. Pan', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '19. Pressure cooker', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '20. Thermos flask', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '21. Vacuum cleaner', NULL FROM categories c WHERE c.name='II. Househould appliances';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '22. Water purifier', NULL FROM categories c WHERE c.name='II. Househould appliances';

-- III
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '1. Desktop computer', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '2. Laptop', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '3. Monitor', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '4. Phone', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '5. Printer', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '6. Smartwatch', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '7. Tablet', NULL FROM categories c WHERE c.name='III. Electronics, telecommunications';

-- IV
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '1. Electric shaver', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '2. Electric toothbrush', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '3. Facial cleansing device', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '4. Hair dryer', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '5. Hair removal machine', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '6. Hair styling machine', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '7. Massage chair', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '8. Scale', NULL FROM categories c WHERE c.name='IV. Beauty, personal care';

-- V
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '1. Camera', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '2. Charger, cable', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '3. Computer mouse', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '4. Headphone', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '5. Keyboard', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '6. Loudspeaker', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '7. Phone case', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '8. Power bank', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '9. Projector', NULL FROM categories c WHERE c.name='V. Accessories';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '10. USB', NULL FROM categories c WHERE c.name='V. Accessories';

-- VI
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '1. Bicycle', NULL FROM categories c WHERE c.name='VI. Other products';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '2. Helmet', NULL FROM categories c WHERE c.name='VI. Other products';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '3. Socket', NULL FROM categories c WHERE c.name='VI. Other products';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '4. Tree trimming machine', NULL FROM categories c WHERE c.name='VI. Other products';
INSERT INTO sub_categories (category_id, name, image_url) SELECT c.id, '5. Water pump', NULL FROM categories c WHERE c.name='VI. Other products';

-- ===============================
-- Sample user for review/demo
-- password: Password123! (placeholder hash)
-- ===============================
INSERT INTO users (username, fullname, email, password_hash, phone_number, address, role, status)
VALUES ('demo_user', 'Demo User', 'demo@example.com', '$2a$10$Z0Y5kR2K2r8PqPyy8xX6o.2n9wP4Iu2HfGQJw4aL7q8y6QYJ7oGzS', '0900000000', 'HCMC', 'user', 'active');

-- ===============================
-- Products: at least 20, with 10 in same subcategory (Air conditional)
-- ===============================
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Midea Inverter 1 HP MAFA-09CDN8', 'Midea', '/images/ac/midea-09cdn8-1.jpg', 'Dong may lanh tiet kiem dien phu hop phong nho.', 5190000.00, 120, 24, 'Co inverter, gas R-32', 4.90, 46500, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Daikin Inverter 1 HP', 'Daikin', '/images/ac/daikin-1hp.jpg', 'Lam lanh nhanh, do on thap.', 8990000.00, 80, 24, '1 HP, R-32', 4.80, 21000, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Panasonic Inverter 1.5 HP', 'Panasonic', '/images/ac/pana-15hp.jpg', 'Cong nghe tiet kiem dien va loc bui min.', 12290000.00, 60, 24, '1.5 HP, Nanoe', 4.85, 18400, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh LG Dual Inverter 1 HP', 'LG', '/images/ac/lg-1hp.jpg', 'Dual inverter, tiet kiem dien.', 9990000.00, 72, 24, '1 HP, Dual inverter', 4.70, 15900, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Samsung WindFree 1 HP', 'Samsung', '/images/ac/samsung-windfree.jpg', 'Gio thoai mai, do on thap.', 10990000.00, 54, 24, '1 HP, WindFree', 4.60, 9800, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Casper Inverter 1 HP', 'Casper', '/images/ac/casper-1hp.jpg', 'Gia tot cho nhu cau co ban.', 6790000.00, 90, 24, '1 HP, inverter', 4.40, 13200, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Aqua Inverter 1 HP', 'Aqua', '/images/ac/aqua-1hp.jpg', 'May lanh pho thong cho can ho.', 6290000.00, 110, 24, '1 HP, R-32', 4.35, 8500, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Toshiba Inverter 1 HP', 'Toshiba', '/images/ac/toshiba-1hp.jpg', 'Dong co ben bi, hoat dong em.', 7590000.00, 76, 24, '1 HP, inverter', 4.50, 9400, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Sharp Inverter 1 HP', 'Sharp', '/images/ac/sharp-1hp.jpg', 'Lam lanh nhanh, tui tien.', 7390000.00, 68, 24, '1 HP, gas R-32', 4.45, 10200, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May lanh Funiki 1 HP', 'Funiki', '/images/ac/funiki-1hp.jpg', 'Mau co ban cho phong duoi 15m2.', 5590000.00, 50, 24, '1 HP', 4.20, 7800, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air conditional';

-- Other 10 products (different subcategories)
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May rua chen Bosch SMS6', 'Bosch', '/images/dw/bosch-sms6.jpg', 'May rua chen gia dinh.', 17990000.00, 20, 24, '15 bo, tiet kiem nuoc', 4.60, 2400, 'in_stock' FROM sub_categories sc WHERE sc.name='2. Dishwasher';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Lo vi song Sharp R-G272VN', 'Sharp', '/images/micro/sharp-rg272.jpg', 'Lo vi song da nang.', 2590000.00, 35, 12, '20L', 4.30, 6200, 'in_stock' FROM sub_categories sc WHERE sc.name='3. Micro';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Tu lanh Samsung Inverter 340L', 'Samsung', '/images/ref/samsung-340l.jpg', 'Tu lanh tiet kiem dien.', 10990000.00, 22, 24, '340L', 4.50, 7300, 'in_stock' FROM sub_categories sc WHERE sc.name='4. Refrigerator';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Loa Bluetooth JBL Charge 5', 'JBL', '/images/speaker/jbl-charge5.jpg', 'Loa di dong chong nuoc.', 3990000.00, 40, 12, 'Bluetooth 5.1', 4.70, 9100, 'in_stock' FROM sub_categories sc WHERE sc.name='5. Speaker';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Smart Tivi LG 55 inch UHD', 'LG', '/images/tv/lg-55.jpg', 'Tivi thong minh 4K.', 11990000.00, 28, 24, '55 inch, 4K', 4.55, 12000, 'in_stock' FROM sub_categories sc WHERE sc.name='6. Tivi';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'May giat Samsung 9.5kg', 'Samsung', '/images/wm/ss-95kg.jpg', 'May giat cua tren.', 6790000.00, 30, 24, '9.5kg', 4.40, 9800, 'in_stock' FROM sub_categories sc WHERE sc.name='7. Washing machine';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Binh nuoc nong Ariston 30L', 'Ariston', '/images/wh/ariston-30l.jpg', 'Binh nuoc nong giu nhiet tot.', 3190000.00, 18, 24, '30L', 4.25, 4200, 'in_stock' FROM sub_categories sc WHERE sc.name='8. Water heater';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Noi chien khong dau Philips HD9252', 'Philips', '/images/af/philips-hd9252.jpg', 'Noi chien dung tich 4.1L.', 2590000.00, 42, 12, '4.1L', 4.65, 15000, 'in_stock' FROM sub_categories sc WHERE sc.name='1. Air fryer';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Laptop Asus Vivobook 15', 'Asus', '/images/laptop/vivobook15.jpg', 'Laptop hoc tap van phong.', 13990000.00, 16, 24, 'i5, 8GB RAM', 4.35, 6700, 'in_stock' FROM sub_categories sc WHERE sc.name='2. Laptop';
INSERT INTO products (category_id, sub_category_id, name, brand, thumbnail_url, description, unit_price, stock_quantity, warranty_period_months, specifications, average_rating, total_sold, status)
SELECT sc.category_id, sc.id, 'Tai nghe Sony WH-CH520', 'Sony', '/images/headphone/sony-ch520.jpg', 'Tai nghe bluetooth pin lau.', 1290000.00, 55, 12, 'Bluetooth', 4.50, 8200, 'in_stock' FROM sub_categories sc WHERE sc.name='4. Headphone';

-- ===============================
-- Product images (min 1 image/product)
-- ===============================
INSERT INTO product_images (product_id, image_url, is_primary)
SELECT p.id, p.thumbnail_url, TRUE
FROM products p
WHERE p.thumbnail_url IS NOT NULL;

-- ===============================
-- Service Packages
-- ===============================
-- Default package for every product
INSERT INTO service_packages (product_id, name, price)
SELECT p.id, 'Goi tieu chuan', p.unit_price
FROM products p;

-- Extra packages for AC product #123-like (Midea sample)
INSERT INTO service_packages (product_id, name, price)
SELECT p.id, 'Goi mien phi cong lap dat', 6190000.00
FROM products p
WHERE p.name='May lanh Midea Inverter 1 HP MAFA-09CDN8';

INSERT INTO service_packages (product_id, name, price)
SELECT p.id, 'Goi mo rong bao hiem', 7730000.00
FROM products p
WHERE p.name='May lanh Midea Inverter 1 HP MAFA-09CDN8';

-- ===============================
-- Spec Groups + Spec Labels (sample detailed for Air conditional)
-- ===============================
INSERT INTO spec_groups (sub_category_id, name)
SELECT sc.id, 'Thong tin chung'
FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO spec_groups (sub_category_id, name)
SELECT sc.id, 'Muc tieu thu dien nang'
FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO spec_groups (sub_category_id, name)
SELECT sc.id, 'Kha nang loc khong khi'
FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO spec_groups (sub_category_id, name)
SELECT sc.id, 'Cong nghe lam lanh'
FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO spec_groups (sub_category_id, name)
SELECT sc.id, 'Tien ich'
FROM sub_categories sc WHERE sc.name='1. Air conditional';
INSERT INTO spec_groups (sub_category_id, name)
SELECT sc.id, 'Thong so lap dat'
FROM sub_categories sc WHERE sc.name='1. Air conditional';

-- Labels for group "Thong tin chung"
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Loai may'
FROM spec_groups sg WHERE sg.name='Thong tin chung' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Cong suat lam lanh'
FROM spec_groups sg WHERE sg.name='Thong tin chung' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Pham vi lam lanh hieu qua'
FROM spec_groups sg WHERE sg.name='Thong tin chung' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Do on trung binh'
FROM spec_groups sg WHERE sg.name='Thong tin chung' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Noi san xuat'
FROM spec_groups sg WHERE sg.name='Thong tin chung' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Loai gas'
FROM spec_groups sg WHERE sg.name='Thong tin chung' LIMIT 1;

-- Labels for group "Muc tieu thu dien nang"
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Tieu thu dien'
FROM spec_groups sg WHERE sg.name='Muc tieu thu dien nang' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Nhan nang luong'
FROM spec_groups sg WHERE sg.name='Muc tieu thu dien nang' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Cong nghe tiet kiem dien'
FROM spec_groups sg WHERE sg.name='Muc tieu thu dien nang' LIMIT 1;

-- Labels for group "Kha nang loc khong khi"
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Loc bui, khang khuan, khu mui'
FROM spec_groups sg WHERE sg.name='Kha nang loc khong khi' LIMIT 1;

-- Labels for group "Cong nghe lam lanh"
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Che do gio'
FROM spec_groups sg WHERE sg.name='Cong nghe lam lanh' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Cong nghe lam lanh nhanh'
FROM spec_groups sg WHERE sg.name='Cong nghe lam lanh' LIMIT 1;

-- Labels for group "Tien ich"
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Tien ich'
FROM spec_groups sg WHERE sg.name='Tien ich' LIMIT 1;

-- Labels for group "Thong so lap dat"
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Kich thuoc dan lanh'
FROM spec_groups sg WHERE sg.name='Thong so lap dat' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Khoi luong dan lanh'
FROM spec_groups sg WHERE sg.name='Thong so lap dat' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Kich thuoc dan nong'
FROM spec_groups sg WHERE sg.name='Thong so lap dat' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Khoi luong dan nong'
FROM spec_groups sg WHERE sg.name='Thong so lap dat' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Chieu dai lap dat ong dong'
FROM spec_groups sg WHERE sg.name='Thong so lap dat' LIMIT 1;
INSERT INTO spec_labels (spec_group_id, name)
SELECT sg.id, 'Chieu cao lap dat toi da'
FROM spec_groups sg WHERE sg.name='Thong so lap dat' LIMIT 1;

-- ===============================
-- Promotion & Review sample
-- ===============================
INSERT INTO promotions (content, expired_date)
VALUES
('Tang voucher 300.000d khi thanh toan online', '2026-12-31'),
('Mien phi van chuyen noi thanh', '2026-09-30');

INSERT INTO promotion_products (promotion_id, product_id)
SELECT 1, p.id FROM products p WHERE p.name='May lanh Midea Inverter 1 HP MAFA-09CDN8' LIMIT 1;
INSERT INTO promotion_products (promotion_id, product_id)
SELECT 2, p.id FROM products p WHERE p.name='Smart Tivi LG 55 inch UHD' LIMIT 1;

INSERT INTO reviews (user_id, product_id, rating, comment)
SELECT u.id, p.id, 5, 'San pham tot, lam lanh nhanh va em.'
FROM users u, products p
WHERE u.username='demo_user'
  AND p.name='May lanh Midea Inverter 1 HP MAFA-09CDN8'
LIMIT 1;

INSERT INTO review_images (review_id, image_url)
SELECT r.id, '/images/review/ac-review-1.jpg'
FROM reviews r
ORDER BY r.id DESC
LIMIT 1;

