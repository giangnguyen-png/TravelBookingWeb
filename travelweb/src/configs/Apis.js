import axios from "axios";
import cookies from 'react-cookies';

export const endpoints = {
    'services': '/provider/services',
    'hotels': '/hotels',
    'tours': '/tours',
    'flights': '/flights',
    'busTrips': '/bus-trips',
    'bookings': '/bookings',
    'my-bookings': '/bookings/me',
    'payments': '/payments',
    'reviews': '/reviews',
    'register': '/auth/register',
    'login': '/auth/login',
    'current-user': '/users/me',
    'locations': '/locations',
    'compare': '/compare',
    'provider-profile': '/provider/profile',
    'provider-statistics': '/provider/statistics',
    'provider-bookings': '/provider/bookings',
    'provider-rooms': '/provider/rooms',
    'rooms': '/rooms',
    'provider-reviews': '/providers/:providerId/reviews',
    'hotel-rooms': '/hotels/:hotelId/rooms'
};

export const authApis = () => {
    return axios.create({
        baseURL: "http://localhost:8080/TravelBookingWeb/api/",
        headers: {
            'Authorization': `Bearer ${cookies.load('token')}`
        }
    });
};

export default axios.create({
    baseURL: "http://localhost:8080/TravelBookingWeb/api/"
});
