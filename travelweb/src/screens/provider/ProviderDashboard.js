import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Alert, Button, Card, Col, Nav, Row } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import cookies from 'react-cookies';
import { MyDispatchContext, MyUserContext } from '../../configs/MyContext';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import ProviderBookings from './ProviderBookings';
import ProviderServices from './ProviderServices';
import ProviderStats from './ProviderStats';

const labels = {
    HOTEL: 'Khách sạn',
    TOUR_COMPANY: 'Công ty tour',
    AIRLINE: 'Hãng bay',
    BUS_COMPANY: 'Nhà xe'
};

const emptyService = {
    hotelName: '',
    description: '',
    address: '',
    locationId: '',
    title: '',
    departureDate: '',
    durationDays: '',
    price: '',
    availableSlots: '',
    flightCode: '',
    departureTime: '',
    arrivalTime: '',
    availableSeats: '',
    departureLocationId: '',
    destinationLocationId: '',
    arrivalLocationId: ''
};

const emptyRoom = {
    hotelId: '',
    roomName: '',
    roomType: 'STANDARD',
    pricePerNight: '',
    availableRooms: '',
    description: ''
};

const toDateTimeInput = (value) => {
    if (!value) return '';
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return '';
    return date.toISOString().slice(0, 16);
};

const appendFormData = (payload, fileField, file) => {
    const formData = new FormData();
    Object.entries(payload).forEach(([key, value]) => {
        if (value !== undefined && value !== null) formData.append(key, value);
    });
    if (file) formData.append(fileField, file);
    return formData;
};

const ProviderDashboard = () => {
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatchContext);
    const navigate = useNavigate();

    const [activeTab, setActiveTab] = useState('overview');
    const [providerProfile, setProviderProfile] = useState(null);
    const [locations, setLocations] = useState([]);
    const [stats, setStats] = useState({ revenue: 0, bookings: 0 });
    const [fromDate] = useState('');
    const [toDate] = useState('');
    const [services, setServices] = useState([]);
    const [providerBookings, setProviderBookings] = useState([]);
    const [error, setError] = useState('');

    const [showServiceModal, setShowServiceModal] = useState(false);
    const [editingService, setEditingService] = useState(null);
    const [serviceForm, setServiceForm] = useState(emptyService);
    const [thumbnailFile, setThumbnailFile] = useState(null);

    const [showRoomModal, setShowRoomModal] = useState(false);
    const [roomForm, setRoomForm] = useState(emptyRoom);
    const [roomImageFile, setRoomImageFile] = useState(null);

    const [monthlyData, setMonthlyData] = useState([]);
    const [yearlyData, setYearlyData] = useState([]);
    const [timeType, setTimeType] = useState('MONTH');

    const providerId = providerProfile?.id;
    const businessType = providerProfile?.businessType;
    const isHotel = businessType === 'HOTEL';

    const updateService = (field, value) => setServiceForm((current) => ({ ...current, [field]: value }));
    const updateRoom = (field, value) => setRoomForm((current) => ({ ...current, [field]: value }));

    const resetServiceModal = () => {
        setEditingService(null);
        setServiceForm(emptyService);
        setThumbnailFile(null);
        setError('');
    };

    const resetRoomModal = () => {
        setRoomForm(emptyRoom);
        setRoomImageFile(null);
        setError('');
    };

    const closeServiceModal = () => {
        setShowServiceModal(false);
        resetServiceModal();
    };

    const closeRoomModal = () => {
        setShowRoomModal(false);
        resetRoomModal();
    };

    const fetchProviderProfile = async () => {
        try {
            const res = await authApis().get(endpoints['provider-profile']);
            setProviderProfile(res.data);
        } catch (err) {
            setError('Không tìm thấy h� sơ nhà cung cấp.');
        }
    };

    const fetchLocations = async () => {
        try {
            const res = await Apis.get(endpoints.locations);
            setLocations(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('L�i lấy ��9a �iỒm:', err);
        }
    };

    const fetchStats = useCallback(async () => {
        if (!providerId) return;
        try {
            const res = await authApis().get(endpoints['provider-statistics'], {
                params: { providerId, fromDate, toDate }
            });
            setStats(res.data);
        } catch (err) {
            console.error('L�i lấy th�ng kê t�"ng quan:', err);
        }
    }, [providerId, fromDate, toDate]);

    const fetchAdvancedStatsFromBE = useCallback(async () => {
        if (!providerId) return;
        try {
            const res = await authApis().get(endpoints['provider-bookings'], {
                params: { providerId }
            });
            const bookingsFromBE = Array.isArray(res.data) ? res.data : [];
            const validBookings = bookingsFromBE.filter((b) => b.status === 'PAID' || b.status === 'COMPLETED');
            const currentYear = new Date().getFullYear();
            const monthlyMap = {};
            const yearlyMap = {};

            for (let m = 1; m <= 12; m++) {
                monthlyMap[m] = { month: m, revenue: 0, count: 0 };
            }

            validBookings.forEach((booking) => {
                const dateObj = new Date(booking.createdAt || booking.created_at);
                if (Number.isNaN(dateObj.getTime())) return;

                const year = dateObj.getFullYear();
                const month = dateObj.getMonth() + 1;
                const price = Number(booking.totalPrice || booking.total_price || 0);

                if (year === currentYear) {
                    monthlyMap[month].revenue += price;
                    monthlyMap[month].count += 1;
                }

                if (!yearlyMap[year]) {
                    yearlyMap[year] = { year, revenue: 0, count: 0 };
                }
                yearlyMap[year].revenue += price;
                yearlyMap[year].count += 1;
            });

            setProviderBookings(bookingsFromBE);
            setMonthlyData(Object.values(monthlyMap));
            setYearlyData(Object.values(yearlyMap).sort((a, b) => a.year - b.year));
        } catch (err) {
            console.error('L�i lấy mảng �ơn hàng vẽ biỒu ��:', err);
        }
    }, [providerId]);

    const fetchServices = useCallback(async () => {
        if (!providerId) return;
        try {
            const res = await authApis().get(endpoints.services, { params: { providerId } });
            setServices(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('L�i lấy d�9ch vụ:', err);
        }
    }, [providerId]);

    useEffect(() => {
        if (user) {
            fetchProviderProfile();
            fetchLocations();
        }
    }, [user]);

    useEffect(() => {
        if (providerId) {
            fetchStats();
            fetchServices();
            fetchAdvancedStatsFromBE();
        }
    }, [providerId, fetchStats, fetchServices, fetchAdvancedStatsFromBE]);

    const logout = () => {
        if (window.confirm('Bạn có chắc chắn mu�n �Ēng xuất?')) {
            cookies.remove('token', { path: '/' });
            cookies.remove('user', { path: '/' });
            dispatch({ type: 'logout' });
            navigate('/user');
        }
    };

    const openAddService = () => {
        resetServiceModal();
        setShowServiceModal(true);
    };

    const openEditService = (service) => {
        setEditingService(service);
        setThumbnailFile(null);
        setServiceForm({
            ...emptyService,
            hotelName: service.hotelName || '',
            description: service.description || '',
            address: service.address || '',
            title: service.title || '',
            departureDate: toDateTimeInput(service.departureDate),
            durationDays: service.durationDays || '',
            price: service.price || '',
            availableSlots: service.availableSlots || '',
            flightCode: service.flightCode || '',
            departureTime: toDateTimeInput(service.departureTime),
            arrivalTime: toDateTimeInput(service.arrivalTime),
            availableSeats: service.availableSeats || ''
        });
        setShowServiceModal(true);
    };

    const openRoomModal = (hotel) => {
        resetRoomModal();
        setRoomForm({ ...emptyRoom, hotelId: String(hotel.id) });
        setShowRoomModal(true);
    };

    const buildPayload = () => {
        const base = { providerId: String(providerId) };
        if (businessType === 'HOTEL') {
            return {
                ...base,
                hotelName: serviceForm.hotelName,
                description: serviceForm.description,
                address: serviceForm.address,
                locationId: serviceForm.locationId || editingService?.locationId?.id || ''
            };
        }
        if (businessType === 'TOUR_COMPANY') {
            return {
                ...base,
                title: serviceForm.title,
                description: serviceForm.description,
                departureDate: serviceForm.departureDate,
                durationDays: serviceForm.durationDays,
                price: serviceForm.price,
                availableSlots: serviceForm.availableSlots,
                departureLocationId: serviceForm.departureLocationId,
                destinationLocationId: serviceForm.destinationLocationId
            };
        }
        if (businessType === 'AIRLINE') {
            return {
                ...base,
                flightCode: serviceForm.flightCode,
                departureTime: serviceForm.departureTime,
                arrivalTime: serviceForm.arrivalTime,
                price: serviceForm.price,
                availableSeats: serviceForm.availableSeats,
                departureLocationId: serviceForm.departureLocationId,
                arrivalLocationId: serviceForm.arrivalLocationId
            };
        }
        return {
            ...base,
            departureTime: serviceForm.departureTime,
            arrivalTime: serviceForm.arrivalTime,
            price: serviceForm.price,
            availableSeats: serviceForm.availableSeats,
            departureLocationId: serviceForm.departureLocationId,
            arrivalLocationId: serviceForm.arrivalLocationId
        };
    };

    const createRoom = async (hotelId = roomForm.hotelId) => {
        const payload = {
            hotelId: String(hotelId),
            roomName: roomForm.roomName,
            roomType: roomForm.roomType,
            pricePerNight: roomForm.pricePerNight,
            availableRooms: roomForm.availableRooms,
            description: roomForm.description,
            image: ''
        };
        await authApis().post(endpoints['provider-rooms'], appendFormData(payload, 'imageFile', roomImageFile), {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    };

    const saveService = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = appendFormData(buildPayload(), 'thumbnailFile', thumbnailFile);
            const config = { headers: { 'Content-Type': 'multipart/form-data' } };
            const res = editingService
                ? await authApis().put(`${endpoints.services}/${editingService.id}`, data, config)
                : await authApis().post(endpoints.services, data, config);

            if (!editingService && isHotel) {
                await createRoom(res.data.id);
            }

            closeServiceModal();
            fetchServices();
            alert(editingService ? 'Cập nhật d�9ch vụ thành công!' : 'Thêm d�9ch vụ thành công!');
        } catch (err) {
            setError(err.response?.data || 'Không thỒ lưu d�9ch vụ.');
        }
    };

    const saveRoom = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await createRoom();
            closeRoomModal();
            alert('Thêm phòng thành công!');
        } catch (err) {
            setError(err.response?.data || 'Không thỒ thêm phòng.');
        }
    };

    const deleteService = async (id) => {
        if (!window.confirm('Bạn có chắc chắn mu�n xóa d�9ch vụ này?')) return;
        try {
            await authApis().delete(`${endpoints.services}/${id}`, { params: { providerId } });
            fetchServices();
            alert('Xóa thành công!');
        } catch (err) {
            alert(err.response?.data || 'Không thỒ xóa d�9ch vụ.');
        }
    };

    if (!user || user.role !== 'PROVIDER') {
        return (
            <div className="container mt-5 text-center">
                <Card className="p-4 border-danger">
                    <h4 className="text-danger">Không có quyền truy cập!</h4>
                    <p className="text-muted">Vui lòng �Ēng nhập tài khoản nhà cung cấp.</p>
                </Card>
            </div>
        );
    }

    return (
        <Row className="w-100 m-0" style={{ minHeight: '100vh' }}>
            <Col md={3} className="bg-dark text-white p-4 d-flex flex-column justify-content-between">
                <div>
                    <h1 className="text-center mb-4">Travel Provider</h1>
                    <hr className="bg-secondary" />
                    <Nav className="flex-column">
                        <Nav.Link onClick={() => setActiveTab('overview')} className={`text-white mb-3 ${activeTab === 'overview' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>T�"ng quan</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('services')} className={`text-white mb-3 ${activeTab === 'services' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Quản lý d�9ch vụ</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('bookings')} className={`text-white mb-3 ${activeTab === 'bookings' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Đơn �ặt hàng</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('stats')} className={`text-white mb-3 ${activeTab === 'stats' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Th�ng kê</Nav.Link>
                    </Nav>
                </div>
                <div><hr className="bg-secondary" /><Button variant="danger" className="w-100 fw-bold" onClick={logout}>ĐĒng xuất</Button></div>
            </Col>

            <Col md={9} className="p-4 bg-light">
                {error && <Alert variant="danger">{error}</Alert>}
                {activeTab === 'overview' && (
                    <div>
                        <h3 className="mb-2 fw-bold">T�"ng quan h�! th�ng</h3>
                        <p className="text-muted mb-4">Loại hình: <strong>{labels[businessType] || 'Đang tải...'}</strong></p>
                        <Row>
                            <Col md={6} className="mb-3"><Card className="p-3 shadow-sm border-0 bg-white"><Card.Body><h6 className="text-muted">T�NG DOANH THU</h6><h3 className="text-primary fw-bold">{stats.revenue?.toLocaleString() || 0} VNĐ</h3></Card.Body></Card></Col>
                            <Col md={6} className="mb-3"><Card className="p-3 shadow-sm border-0 bg-white"><Card.Body><h6 className="text-muted">T�NG ĐƠN ĐẶT D�`CH VỤ</h6><h3 className="text-success fw-bold">{stats.bookings || 0} �ơn</h3></Card.Body></Card></Col>
                        </Row>
                    </div>
                )}
                {activeTab === 'services' && (
                    <ProviderServices
                        labels={labels}
                        businessType={businessType}
                        isHotel={isHotel}
                        services={services}
                        locations={locations}
                        showServiceModal={showServiceModal}
                        editingService={editingService}
                        serviceForm={serviceForm}
                        roomForm={roomForm}
                        showRoomModal={showRoomModal}
                        openAddService={openAddService}
                        openEditService={openEditService}
                        openRoomModal={openRoomModal}
                        deleteService={deleteService}
                        updateService={updateService}
                        updateRoom={updateRoom}
                        setThumbnailFile={setThumbnailFile}
                        setRoomImageFile={setRoomImageFile}
                        saveService={saveService}
                        saveRoom={saveRoom}
                        closeServiceModal={closeServiceModal}
                        closeRoomModal={closeRoomModal}
                    />
                )}
                {activeTab === 'bookings' && <ProviderBookings providerBookings={providerBookings} />}
                {activeTab === 'stats' && (
                    <ProviderStats
                        stats={stats}
                        monthlyData={monthlyData}
                        yearlyData={yearlyData}
                        timeType={timeType}
                        setTimeType={setTimeType}
                    />
                )}
            </Col>
        </Row>
    );
};

export default ProviderDashboard;
