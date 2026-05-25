use traveldb;

-- 1. Bảng người dùng --
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    avatar VARCHAR(255),
    role ENUM(
        'CUSTOMER',
        'PROVIDER',
        'ADMIN'
    ) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (full_name, username, email, password, avatar, role) VALUES
('Nguyễn Văn A', 'admin', 'admin@travel.com', '123456', 'avatar_a.png', 'ADMIN'),
('Lê Thị B', 'provider1', 'lethib@travel.com', '123456', 'avatar_b.png', 'PROVIDER'),
('Trần Văn C', 'provider2', 'tranvanc@travel.com', '123456', 'avatar_c.png', 'PROVIDER'),
('Phạm Thị D', 'provider3', 'phamthid@travel.com', '123456', 'avatar_d.png', 'PROVIDER'),
('Hoàng Văn E', 'provider4', 'hoangvane@travel.com', '123456', 'avatar_e.png', 'PROVIDER'),
('Vũ Thị F', 'provider5', 'vuthif@travel.com', '123456', 'avatar_f.png', 'PROVIDER'),
('Đặng Văn G', 'customer1', 'dangvang@gmail.com', '123456', 'avatar_g.png', 'CUSTOMER'),
('Bùi Thị H', 'customer2', 'buithih@gmail.com', '123456', 'avatar_h.png', 'CUSTOMER'),
('Đỗ Văn I', 'customer3', 'dovani@gmail.com', '123456', 'avatar_i.png', 'CUSTOMER'),
('Ngô Thị K', 'customer4', 'ngothik@gmail.com', '123456', 'avatar_k.png', 'CUSTOMER');

-- 2. Bảng nhà cung cấp dịch vụ --
CREATE TABLE provider_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    business_type ENUM(
        'TOUR_COMPANY',
        'HOTEL',
        'AIRLINE',
        'BUS_COMPANY'
    ) NOT NULL,
    verification_status ENUM(
        'PENDING',
        'APPROVED',
        'REJECTED'
    ) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
        REFERENCES users(id)
);

INSERT INTO provider_profiles (user_id, company_name, business_type, verification_status) VALUES
(2, 'Công ty Du lịch Vietravel', 'TOUR_COMPANY', 'APPROVED'),
(3, 'Trung tâm Quản lý Khách sạn & Biệt thự', 'HOTEL', 'APPROVED'),
(4, 'Vietnam Airlines', 'AIRLINE', 'APPROVED'),
(5, 'Xe khách Phương Trang', 'BUS_COMPANY', 'APPROVED'),
(6, 'Saigontourist', 'TOUR_COMPANY', 'PENDING');

-- 3. Bảng địa điểm --
CREATE TABLE locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    province VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL
);

INSERT INTO locations (province, country) VALUES
('Hà Nội', 'Việt Nam'),
('TP. Hồ Chí Minh', 'Việt Nam'),
('Đà Nẵng', 'Việt Nam'),
('Nha Trang', 'Việt Nam'),
('Đà Lạt', 'Việt Nam'),
('Phú Quốc', 'Việt Nam'),
('Sa Pa', 'Việt Nam');

-- 4. Bảng tour du lịch --
CREATE TABLE tours (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    departure_location_id BIGINT NOT NULL,
    destination_location_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    departure_date DATETIME NOT NULL,
    duration_days INT NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    available_slots INT NOT NULL,
    thumbnail VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (provider_id)
        REFERENCES provider_profiles(id),
    FOREIGN KEY (departure_location_id)
        REFERENCES locations(id),
    FOREIGN KEY (destination_location_id)
        REFERENCES locations(id)
);

INSERT INTO tours (provider_id, departure_location_id, destination_location_id, title, description, departure_date, duration_days, price, available_slots, thumbnail) VALUES
(1, 1, 7, 'Tour Hà Nội - Sa Pa 3N2Đ', 'Khám phá Fansipan, bản Cát Cát.', '2026-06-10 06:00:00', 3, 2500000.00, 20, 'tour_sapa.jpg'),
(1, 2, 5, 'Tour HCM - Đà Lạt Mộng Mơ', 'Săn mây Đà Lạt, check-in đồi chè.', '2026-06-15 05:00:00', 4, 3000000.00, 15, 'tour_dalat.jpg'),
(1, 1, 3, 'Tour Hà Nội - Đà Nẵng - Hội An', 'Khám phá miền Trung trọn gói.', '2026-07-01 07:00:00', 4, 4500000.00, 25, 'tour_danang.jpg'),
(5, 2, 6, 'Tour HCM - Phú Quốc 3N2Đ', 'Nghỉ dưỡng resort 5 sao, lặn ngắm san hô.', '2026-06-20 08:00:00', 3, 5000000.00, 30, 'tour_phuquoc.jpg'),
(5, 2, 4, 'Tour HCM - Nha Trang Biển Gọi', 'Vui chơi VinWonders, tắm bùn.', '2026-07-05 06:00:00', 3, 3500000.00, 20, 'tour_nhatrang.jpg');


-- 5. Bảng khách sạn --
CREATE TABLE hotels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    hotel_name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(255),
    thumbnail VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (provider_id)
        REFERENCES provider_profiles(id),
    FOREIGN KEY (location_id)
        REFERENCES locations(id)
);

INSERT INTO hotels (provider_id, location_id, hotel_name, description, address, thumbnail) VALUES
(2, 1, 'InterContinental Hanoi Landmark72', 'Khách sạn cao nhất Hà Nội với view toàn cảnh thành phố tuyệt đẹp.', 'Keangnam Landmark72, Phạm Hùng, Hà Nội', 'intercon_hn.jpg'),
(2, 1, 'Sofitel Legend Metropole Hanoi', 'Khách sạn 5 sao mang kiến trúc Pháp cổ kính lịch sử lâu đời.', '15 Ngô Quyền, Hoàn Kiếm, Hà Nội', 'metropole_hn.jpg'),

(2, 2, 'Vinpearl Landmark 81, Autograph Collection', 'Trải nghiệm xa hoa bậc nhất tại tòa nhà cao nhất Việt Nam.', '720A Điện Biên Phủ, Bình Thạnh, TPHCM', 'landmark81_sg.jpg'),
(2, 2, 'Hotel des Arts Saigon - MGallery', 'Khách sạn boutique mang đậm chất nghệ thuật giữa lòng Sài Gòn.', '76-78 Nguyễn Thị Minh Khai, Quận 3, TPHCM', 'hoteldesarts_sg.jpg'),

(2, 3, 'Novotel Danang Premier Han River', 'Vị trí đắc địa ngay trung tâm, ngắm trọn vẹn sông Hàn và cầu Rồng.', '36 Bạch Đằng, Hải Châu, Đà Nẵng', 'novotel_dn.jpg'),
(2, 3, 'Pullman Danang Beach Resort', 'Khu nghỉ dưỡng 5 sao sang trọng với bãi biển riêng tư.', '101 Võ Nguyên Giáp, Ngũ Hành Sơn, Đà Nẵng', 'pullman_dn.jpg'),

(2, 4, 'Amiana Resort Nha Trang', 'Khu nghỉ dưỡng ẩn mình bên vịnh biển xanh mát, nổi tiếng với hồ bơi nước mặn.', 'Phạm Văn Đồng, Vĩnh Hòa, Nha Trang', 'amiana_nt.jpg'),

(2, 5, 'Ana Mandara Villas Dalat Resort & Spa', 'Trải nghiệm nghỉ dưỡng tại các biệt thự Pháp cổ giữa rừng thông.', 'Lê Lai, Phường 5, Đà Lạt', 'anamandara_dl.jpg'),

(2, 6, 'JW Marriott Phu Quoc Emerald Bay', 'Kiệt tác kiến trúc của Bill Bensley tại bãi Kem đẹp nhất đảo ngọc.', 'Bãi Kem, An Thới, Phú Quốc', 'jwmarriott_pq.jpg'),

(2, 7, 'Hotel de la Coupole - MGallery Sapa', 'Sự hòa quyện hoàn hảo giữa sắc màu văn hóa Tây Bắc và kiến trúc Pháp.', '1 Hoàng Liên, Sa Pa', 'coupole_sp.jpg');

-- 6. Bảng phòng khách sạn --
CREATE TABLE hotel_rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_name VARCHAR(255) NOT NULL,
    room_type ENUM(
        'STANDARD',
        'DELUXE',
        'VIP'
    ) NOT NULL,
    price_per_night DECIMAL(12,2) NOT NULL,
    available_rooms INT NOT NULL,
    description TEXT,
    image VARCHAR(255),
    FOREIGN KEY (hotel_id)
        REFERENCES hotels(id)
);

INSERT INTO hotel_rooms (hotel_id, room_name, room_type, price_per_night, available_rooms, description, image) VALUES
(1, 'Phòng Classic King', 'STANDARD', 2500000.00, 10, 'View toàn cảnh thành phố, nội thất hiện đại', 'room_ic_1.jpg'),
(2, 'Phòng Premium', 'DELUXE', 4200000.00, 5, 'Kiến trúc Pháp cổ điển, giường King', 'room_sofitel_1.jpg'),
(3, 'Phòng Panoramic', 'VIP', 6500000.00, 3, 'View sông Sài Gòn từ tầng siêu cao', 'room_lm81.jpg'),
(4, 'Phòng Superior', 'STANDARD', 2800000.00, 8, 'Thiết kế boutique, ban công nhỏ', 'room_arts.jpg'),
(5, 'Phòng Deluxe River View', 'DELUXE', 1800000.00, 15, 'Tầm nhìn trực diện ra cầu Rồng', 'room_novotel.jpg'),
(6, 'Cottage 1 Phòng Ngủ', 'VIP', 5500000.00, 4, 'Biệt thự nhỏ sát biển, hồ bơi riêng', 'room_pullman.jpg'),
(7, 'Phòng Ocean Villa', 'DELUXE', 3500000.00, 6, 'View biển, bồn tắm đá tự nhiên ngoài trời', 'room_amiana.jpg'),
(8, 'Phòng Le Petit', 'STANDARD', 2200000.00, 10, 'Nằm trong tầng áp mái biệt thự Pháp', 'room_ana.jpg'),
(9, 'Phòng Emerald Bay View', 'DELUXE', 7000000.00, 5, 'Ban công hướng biển tuyệt đẹp, nội thất xa xỉ', 'room_jw.jpg'),
(10, 'Phòng Classic Hoàng Liên', 'STANDARD', 3000000.00, 12, 'View thung lũng Mường Hoa, phong cách Đông Dương', 'room_coupole.jpg');


-- 7. Bảng vé máy bay --
CREATE TABLE flights (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    flight_code VARCHAR(50) NOT NULL,
    departure_location_id BIGINT NOT NULL,
    arrival_location_id BIGINT NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    available_seats INT NOT NULL,
    thumbnail VARCHAR(255),
    FOREIGN KEY (provider_id)
        REFERENCES provider_profiles(id),
    FOREIGN KEY (departure_location_id)
        REFERENCES locations(id),
    FOREIGN KEY (arrival_location_id)
        REFERENCES locations(id)
);

INSERT INTO flights (provider_id, flight_code, departure_location_id, arrival_location_id, departure_time, arrival_time, price, available_seats, thumbnail) VALUES
(3, 'VN245', 1, 2, '2026-06-10 08:00:00', '2026-06-10 10:15:00', 1800000.00, 150, 'vna_flight.jpg'),
(3, 'VN250', 2, 1, '2026-06-11 14:00:00', '2026-06-11 16:15:00', 1750000.00, 120, 'vna_flight.jpg'),
(3, 'VN102', 1, 3, '2026-06-15 09:30:00', '2026-06-15 11:00:00', 1200000.00, 100, 'vna_flight.jpg'),
(3, 'VN889', 2, 6, '2026-07-01 07:00:00', '2026-07-01 08:15:00', 1500000.00, 80, 'vna_flight.jpg'),
(3, 'VN331', 3, 2, '2026-07-05 18:00:00', '2026-07-05 19:30:00', 1100000.00, 100, 'vna_flight.jpg');

-- 8. Bảng vé xe --
CREATE TABLE bus_trips (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    provider_id BIGINT NOT NULL,
    departure_location_id BIGINT NOT NULL,
    arrival_location_id BIGINT NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    available_seats INT NOT NULL,
    FOREIGN KEY (provider_id)
        REFERENCES provider_profiles(id),
    FOREIGN KEY (departure_location_id)
        REFERENCES locations(id),
    FOREIGN KEY (arrival_location_id)
        REFERENCES locations(id)
);

INSERT INTO bus_trips (provider_id, departure_location_id, arrival_location_id, departure_time, arrival_time, price, available_seats) VALUES
(4, 2, 5, '2026-06-10 22:00:00', '2026-06-11 05:00:00', 300000.00, 40),
(4, 5, 2, '2026-06-14 23:00:00', '2026-06-15 06:00:00', 300000.00, 40),
(4, 2, 4, '2026-06-20 20:00:00', '2026-06-21 05:00:00', 350000.00, 40),
(4, 1, 7, '2026-06-25 21:30:00', '2026-06-26 04:30:00', 400000.00, 30),
(4, 7, 1, '2026-06-28 13:00:00', '2026-06-28 20:00:00', 400000.00, 30);

-- 9. Bảng booking --
CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    booking_type ENUM(
        'TOUR',
        'HOTEL',
        'TRANSPORT'
    ) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    status ENUM(
        'PENDING',
        'PAID',
        'CANCELLED',
        'COMPLETED'
    ) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id)
        REFERENCES users(id)
);

INSERT INTO bookings (customer_id, booking_type, total_price, status) VALUES
(7, 'TOUR', 5000000.00, 'PAID'),
(8, 'HOTEL', 3600000.00, 'PAID'),
(9, 'TRANSPORT', 1800000.00, 'COMPLETED'),
(10, 'TRANSPORT', 600000.00, 'PENDING'),
(7, 'TOUR', 5000000.00, 'PENDING'),
(8, 'HOTEL', 3500000.00, 'PAID'),
(9, 'TRANSPORT', 1500000.00, 'PAID');


-- 10. Bảng tour booking --
CREATE TABLE tour_bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE NOT NULL,
    tour_id BIGINT NOT NULL,
    number_of_people INT NOT NULL,
    FOREIGN KEY (booking_id)
        REFERENCES bookings(id),
    FOREIGN KEY (tour_id)
        REFERENCES tours(id)
);

INSERT INTO tour_bookings (booking_id, tour_id, number_of_people) VALUES
(1, 1, 2), 
(5, 4, 1);

-- 11. Bảng hotel_booking --
CREATE TABLE hotel_bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_rooms INT NOT NULL,
    FOREIGN KEY (booking_id)
        REFERENCES bookings(id),
    FOREIGN KEY (room_id)
        REFERENCES hotel_rooms(id)
);

INSERT INTO hotel_bookings (booking_id, room_id, check_in_date, check_out_date, number_of_rooms) VALUES
(2, 1, '2026-06-10', '2026-06-13', 1), 
(6, 3, '2026-07-01', '2026-07-02', 1);

-- 12. Bảng hotel_booking--
CREATE TABLE transport_bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE NOT NULL,
    transport_type ENUM(
        'FLIGHT',
        'BUS'
    ) NOT NULL,
    transport_service_id BIGINT NOT NULL,
    seat_number VARCHAR(20),
    FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
);

INSERT INTO transport_bookings (booking_id, transport_type, transport_service_id, seat_number) VALUES
(3, 'FLIGHT', 1, '12A'), 
(4, 'BUS', 1, 'A01, A02'), 
(7, 'FLIGHT', 4, '14B');

-- 13. Bảng thanh toán --
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE NOT NULL,
    payment_method ENUM(
        'CASH',
        'PAYPAL',
        'MOMO',
        'ZALOPAY'
    ) NOT NULL,
    payment_status ENUM(
        'PENDING',
        'PAID',
        'FAILED'
    ) DEFAULT 'PENDING',
    amount DECIMAL(12,2) NOT NULL,
    paid_at DATETIME,
    FOREIGN KEY (booking_id)
        REFERENCES bookings(id)
);

INSERT INTO payments (booking_id, payment_method, payment_status, amount, paid_at) VALUES
(1, 'MOMO', 'PAID', 5000000.00, '2026-06-01 10:00:00'),
(2, 'ZALOPAY', 'PAID', 3600000.00, '2026-06-02 11:30:00'),
(3, 'PAYPAL', 'PAID', 1800000.00, '2026-06-03 09:15:00'),
(4, 'CASH', 'PENDING', 600000.00, NULL),
(5, 'MOMO', 'PENDING', 5000000.00, NULL),
(6, 'ZALOPAY', 'PAID', 3500000.00, '2026-06-04 14:00:00'),
(7, 'CASH', 'PAID', 1500000.00, '2026-06-05 16:45:00');

-- 14. Bảng review --
CREATE TABLE reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id)
        REFERENCES users(id),
    FOREIGN KEY (provider_id)
        REFERENCES provider_profiles(id)
);

INSERT INTO reviews (customer_id, provider_id, rating, comment) VALUES
(7, 1, 5, 'Tour tổ chức rất chuyên nghiệp, hướng dẫn viên nhiệt tình.'),
(8, 2, 4, 'Phòng sạch sẽ, view đẹp nhưng đồ ăn sáng hơi ít món.'),
(9, 3, 5, 'Bay đúng giờ, tiếp viên hàng không chu đáo.'),
(10, 4, 3, 'Tài xế lái cẩn thận, nhưng máy lạnh trên xe hơi lạnh.'),
(7, 5, 5, 'Trải nghiệm tuyệt vời tại Phú Quốc, sẽ quay lại.');