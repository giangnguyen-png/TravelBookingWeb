import axios from "axios";
import cookies from 'react-cookies';

// 1. Khai báo các đường dẫn API từ Backend (Đã bỏ /api ở đầu vì baseURL đã có)
export const endpoints = {
    'services': '/provider/services',
    'hotels': '/hotels',
    'tours': '/tours',
    'flights': '/flights',
    'busTrips': '/bus-trips',
    'bookings': '/bookings',
    'payments': '/payments',
    'reviews': '/reviews',
    'register': '/auth/register',
    'login': '/auth/login',
    'current-user': '/users/me'
};

// 2. Tạo cấu hình Axios có đính kèm Token bảo mật
export const authApis = () => {
    return axios.create({
        baseURL: "http://localhost:8080/TravelBookingWeb/api/",
        headers: {
            'Authorization': `Bearer ${cookies.load('token')}`
        }
    });
};

// 3. Cấu hình Axios cơ bản công khai
export default axios.create({
    baseURL: "http://localhost:8080/TravelBookingWeb/api/"
});