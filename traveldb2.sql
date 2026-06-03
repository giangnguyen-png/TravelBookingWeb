USE traveldb;

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

INSERT INTO users (
    full_name,
    username,
    email,
    password,
    avatar,
    role
) VALUES
(
    'Nguyễn Văn A',
    'admin',
    'admin@travel.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'ADMIN'
),
(
    'Lê Thị B',
    'provider1',
    'lethib@travel.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'PROVIDER'
),
(
    'Trần Văn C',
    'provider2',
    'tranvanc@travel.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'PROVIDER'
),
(
    'Phạm Thị D',
    'provider3',
    'phamthid@travel.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'PROVIDER'
),
(
    'Hoàng Văn E',
    'provider4',
    'hoangvane@travel.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'PROVIDER'
),
(
    'Vũ Thị F',
    'provider5',
    'vuthif@travel.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'PROVIDER'
),
(
    'Đặng Văn G',
    'customer1',
    'dangvang@gmail.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'CUSTOMER'
),
(
    'Bùi Thị H',
    'customer2',
    'buithih@gmail.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'CUSTOMER'
),
(
    'Đỗ Văn I',
    'customer3',
    'dovani@gmail.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'CUSTOMER'
),
(
    'Ngô Thị K',
    'customer4',
    'ngothik@gmail.com',
    '$2a$10$7LM2fB5R9Nz/D6E/YIscueOYtu1tdm4Dz2BKJl3UyNsLNWe9jhlxS',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg',
    'CUSTOMER'
);

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

INSERT INTO provider_profiles (
    user_id,
    company_name,
    business_type,
    verification_status
) VALUES
(2, 'Công ty Du lịch Vietravel', 'TOUR_COMPANY', 'APPROVED'),
(3, 'Trung tâm Quản lý Khách sạn & Biệt thự', 'HOTEL', 'APPROVED'),
(4, 'Vietnam Airlines', 'AIRLINE', 'APPROVED'),
(5, 'Xe khách Phương Trang', 'BUS_COMPANY', 'APPROVED'),
(6, 'Saigontourist', 'TOUR_COMPANY', 'PENDING');

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

INSERT INTO tours (
    provider_id,
    departure_location_id,
    destination_location_id,
    title,
    description,
    departure_date,
    duration_days,
    price,
    available_slots,
    thumbnail
) VALUES
(
    1,
    1,
    7,
    'Tour Hà Nội - Sa Pa 3N2Đ',
    'Khám phá Fansipan, bản Cát Cát.',
    '2026-06-10 06:00:00',
    3,
    2500000.00,
    20,
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
),
(
    1,
    2,
    5,
    'Tour HCM - Đà Lạt Mộng Mơ',
    'Săn mây Đà Lạt, check-in đồi chè.',
    '2026-06-15 05:00:00',
    4,
    3000000.00,
    15,
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
);

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

INSERT INTO hotels (
    provider_id,
    location_id,
    hotel_name,
    description,
    address,
    thumbnail
) VALUES
(
    2,
    1,
    'InterContinental Hanoi Landmark72',
    'Khách sạn cao cấp tại Hà Nội.',
    'Keangnam Landmark72, Hà Nội',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
),
(
    2,
    2,
    'Vinpearl Landmark 81',
    'Khách sạn sang trọng tại TP.HCM.',
    'Bình Thạnh, TP.HCM',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
);

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

INSERT INTO hotel_rooms (
    hotel_id,
    room_name,
    room_type,
    price_per_night,
    available_rooms,
    description,
    image
) VALUES
(
    1,
    'Phòng Classic King',
    'STANDARD',
    2500000.00,
    10,
    'View toàn cảnh thành phố.',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
),
(
    2,
    'Phòng Panoramic',
    'VIP',
    6500000.00,
    3,
    'View thành phố cực đẹp.',
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
);

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

INSERT INTO flights (
    provider_id,
    flight_code,
    departure_location_id,
    arrival_location_id,
    departure_time,
    arrival_time,
    price,
    available_seats,
    thumbnail
) VALUES
(
    3,
    'VN245',
    1,
    2,
    '2026-06-10 08:00:00',
    '2026-06-10 10:15:00',
    1800000.00,
    150,
    'https://res.cloudinary.com/dhdae2pwk/image/upload/v1768307214/media/courses/1_fk2vep.jpg'
);

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

INSERT INTO bus_trips (
    provider_id,
    departure_location_id,
    arrival_location_id,
    departure_time,
    arrival_time,
    price,
    available_seats
) VALUES
(
    4,
    2,
    5,
    '2026-06-10 22:00:00',
    '2026-06-11 05:00:00',
    300000.00,
    40
);

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

INSERT INTO bookings (
    customer_id,
    booking_type,
    total_price,
    status
) VALUES
(7, 'TOUR', 5000000.00, 'PAID'),
(8, 'HOTEL', 3600000.00, 'PAID');

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

INSERT INTO tour_bookings (
    booking_id,
    tour_id,
    number_of_people
) VALUES
(1, 1, 2);

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

INSERT INTO hotel_bookings (
    booking_id,
    room_id,
    check_in_date,
    check_out_date,
    number_of_rooms
) VALUES
(2, 1, '2026-06-10', '2026-06-13', 1);

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

INSERT INTO reviews (
    customer_id,
    provider_id,
    rating,
    comment
) VALUES
(
    7,
    1,
    5,
    'Tour tổ chức rất chuyên nghiệp.'
),
(
    8,
    2,
    4,
    'Khách sạn sạch sẽ và view đẹp.'
);