import React, { useCallback, useContext, useEffect, useState } from 'react';
import { Alert, Button, Card, Col, Form, Modal, Nav, Row, Table } from 'react-bootstrap';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { useNavigate } from 'react-router-dom';
import cookies from 'react-cookies';
import { MyDispatchContext, MyUserContext } from '../../configs/MyContext';
import Apis, { authApis } from '../../configs/Apis';

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

const serviceName = (service, businessType) => {
    if (businessType === 'HOTEL') return service.hotelName;
    if (businessType === 'TOUR_COMPANY') return service.title;
    if (businessType === 'AIRLINE') return service.flightCode;
    return `Chuyến xe #${service.id}`;
};

const ProviderDashboard = () => {
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatchContext);
    const navigate = useNavigate();

    const [activeTab, setActiveTab] = useState('overview');
    const [providerProfile, setProviderProfile] = useState(null);
    const [locations, setLocations] = useState([]);
    const [stats, setStats] = useState({ revenue: 0, bookings: 0 });
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [services, setServices] = useState([]);
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

    const fetchProviderProfile = async () => {
        try {
            const res = await authApis().get('/provider/profile');
            setProviderProfile(res.data);
        } catch (err) {
            setError('Không tìm thấy hồ sơ nhà cung cấp.');
        }
    };

    const fetchLocations = async () => {
        try {
            const res = await Apis.get('/locations');
            setLocations(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('Lỗi lấy địa điểm:', err);
        }
    };

    const fetchStats = useCallback(async () => {
    if (!providerId) return;
    try {
        const res = await authApis().get('/provider/statistics', {
            params: { providerId, fromDate, toDate }
        });
        setStats(res.data); // Hứng { revenue, bookings } từ BE
    } catch (err) {
        console.error('Lỗi lấy thống kê tổng quan:', err);
    }
}, [providerId, fromDate, toDate]);

    const fetchAdvancedStatsFromBE = useCallback(async () => {
    if (!providerId) return;
    try {
        // Gọi chuẩn API đơn hàng mà BE đã viết sẵn cho Provider
        const res = await authApis().get('/provider/bookings', {
            params: { providerId }
        });
        const bookingsFromBE = Array.isArray(res.data) ? res.data : [];

        // Lọc các đơn hàng hợp lệ (đã thanh toán hoặc hoàn thành) dựa theo ENUM status của bạn
        const validBookings = bookingsFromBE.filter(b => b.status === 'PAID' || b.status === 'COMPLETED');

        const currentYear = new Date().getFullYear();
        
        // Cấu trúc mảng 12 tháng chuẩn để nạp vào biểu đồ cột
        const monthlyMap = {};
        for (let m = 1; m <= 12; m++) {
            monthlyMap[m] = { month: m, revenue: 0, count: 0 };
        }
        
        const yearlyMap = {};

    validBookings.forEach(booking => {
            const dateObj = new Date(booking.createdAt || booking.created_at);
            if (Number.isNaN(dateObj.getTime())) return;

            const year = dateObj.getFullYear();
            const month = dateObj.getMonth() + 1;
            const price = Number(booking.totalPrice || booking.total_price || 0);

            // Gom nhóm vào tháng tương ứng nếu thuộc năm hiện tại
            if (year === currentYear) {
                monthlyMap[month].revenue += price;
                monthlyMap[month].count += 1;
            }

            // Gom nhóm vào năm tương ứng trong lịch sử
            if (!yearlyMap[year]) {
                yearlyMap[year] = { year: year, revenue: 0, count: 0 };
            }
            yearlyMap[year].revenue += price;
            yearlyMap[year].count += 1;
        });

        // Đẩy dữ liệu đã gom nhóm từ API của BE vào State biểu đồ
        setMonthlyData(Object.values(monthlyMap));
        setYearlyData(Object.values(yearlyMap).sort((a, b) => a.year - b.year));

    } catch (err) {
        console.error('Lỗi lấy mảng đơn hàng vẽ biểu đồ:', err);
    }
}, [providerId]);

    const fetchServices = useCallback(async () => {
        if (!providerId) return;
        try {
            const res = await authApis().get('/provider/services', { params: { providerId } });
            setServices(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error('Lỗi lấy dịch vụ:', err);
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
        fetchStats();                  // Chạy API số tổng của bạn
        fetchServices();               // Chạy API danh sách dịch vụ của bạn
        fetchAdvancedStatsFromBE();    // Chạy API lấy mảng đơn hàng để vẽ biểu đồ
    }
}, [providerId, fetchStats, fetchServices, fetchAdvancedStatsFromBE]);

    const logout = () => {
        if (window.confirm('Bạn có chắc chắn muốn đăng xuất?')) {
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

    const saveService = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const data = appendFormData(buildPayload(), 'thumbnailFile', thumbnailFile);
            const config = { headers: { 'Content-Type': 'multipart/form-data' } };
            const res = editingService
                ? await authApis().put(`/provider/services/${editingService.id}`, data, config)
                : await authApis().post('/provider/services', data, config);

            if (!editingService && isHotel) {
                await createRoom(res.data.id);
            }

            setShowServiceModal(false);
            resetServiceModal();
            fetchServices();
            alert(editingService ? 'Cập nhật dịch vụ thành công!' : 'Thêm dịch vụ thành công!');
        } catch (err) {
            setError(err.response?.data || 'Không thể lưu dịch vụ.');
        }
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
        await authApis().post('/provider/rooms', appendFormData(payload, 'imageFile', roomImageFile), {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    };

    const saveRoom = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await createRoom();
            setShowRoomModal(false);
            resetRoomModal();
            alert('Thêm phòng thành công!');
        } catch (err) {
            setError(err.response?.data || 'Không thể thêm phòng.');
        }
    };

    const deleteService = async (id) => {
        if (!window.confirm('Bạn có chắc chắn muốn xóa dịch vụ này?')) return;
        try {
            await authApis().delete(`/provider/services/${id}`, { params: { providerId } });
            fetchServices();
            alert('Xóa thành công!');
        } catch (err) {
            alert(err.response?.data || 'Không thể xóa dịch vụ.');
        }
    };

    const locationOptions = (
        <>
            <option value="">Chọn địa điểm</option>
            {locations.map((location) => (
                <option key={location.id} value={location.id}>
                    {[location.province, location.country].filter(Boolean).join(', ')}
                </option>
            ))}
        </>
    );

    const imageInput = (required) => (
        <Form.Group className="mb-3">
            <Form.Label>Ảnh đại diện</Form.Label>
            <Form.Control required={required} type="file" accept="image/*" onChange={(e) => setThumbnailFile(e.target.files[0] || null)} />
        </Form.Group>
    );

    const roomFields = (
        <Card className="border-0 bg-light mt-4">
            <Card.Body>
                <h5 className="fw-bold mb-3">Thông tin phòng khách sạn</h5>
                <Form.Group className="mb-3">
                    <Form.Label>Tên phòng</Form.Label>
                    <Form.Control required value={roomForm.roomName} onChange={(e) => updateRoom('roomName', e.target.value)} />
                </Form.Group>
                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Loại phòng</Form.Label>
                            <Form.Select required value={roomForm.roomType} onChange={(e) => updateRoom('roomType', e.target.value)}>
                                <option value="STANDARD">STANDARD</option>
                                <option value="DELUXE">DELUXE</option>
                                <option value="VIP">VIP</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Giá mỗi đêm</Form.Label>
                            <Form.Control required min="1" type="number" value={roomForm.pricePerNight} onChange={(e) => updateRoom('pricePerNight', e.target.value)} />
                        </Form.Group>
                    </Col>
                </Row>
                <Form.Group className="mb-3">
                    <Form.Label>Số phòng còn trống</Form.Label>
                    <Form.Control required min="0" type="number" value={roomForm.availableRooms} onChange={(e) => updateRoom('availableRooms', e.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Mô tả phòng</Form.Label>
                    <Form.Control required as="textarea" rows={3} value={roomForm.description} onChange={(e) => updateRoom('description', e.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Ảnh phòng</Form.Label>
                    <Form.Control required type="file" accept="image/*" onChange={(e) => setRoomImageFile(e.target.files[0] || null)} />
                </Form.Group>
            </Card.Body>
        </Card>
    );

    const serviceFields = () => {
        if (businessType === 'HOTEL') {
            return (
                <>
                    <Form.Group className="mb-3">
                        <Form.Label>Tên khách sạn</Form.Label>
                        <Form.Control required value={serviceForm.hotelName} onChange={(e) => updateService('hotelName', e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Mô tả khách sạn</Form.Label>
                        <Form.Control required as="textarea" rows={3} value={serviceForm.description} onChange={(e) => updateService('description', e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Địa chỉ</Form.Label>
                        <Form.Control required value={serviceForm.address} onChange={(e) => updateService('address', e.target.value)} />
                    </Form.Group>
                    {imageInput(!editingService)}
                    <Form.Group className="mb-3">
                        <Form.Label>Địa điểm</Form.Label>
                        <Form.Select required value={serviceForm.locationId} onChange={(e) => updateService('locationId', e.target.value)}>
                            {locationOptions}
                        </Form.Select>
                    </Form.Group>
                    {!editingService && roomFields}
                </>
            );
        }
        if (businessType === 'TOUR_COMPANY') {
            return (
                <>
                    <Form.Group className="mb-3">
                        <Form.Label>Tên tour</Form.Label>
                        <Form.Control required value={serviceForm.title} onChange={(e) => updateService('title', e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Mô tả</Form.Label>
                        <Form.Control required as="textarea" rows={3} value={serviceForm.description} onChange={(e) => updateService('description', e.target.value)} />
                    </Form.Group>
                    <Row>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Ngày khởi hành</Form.Label><Form.Control required type="datetime-local" value={serviceForm.departureDate} onChange={(e) => updateService('departureDate', e.target.value)} /></Form.Group></Col>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Số ngày</Form.Label><Form.Control required min="1" type="number" value={serviceForm.durationDays} onChange={(e) => updateService('durationDays', e.target.value)} /></Form.Group></Col>
                    </Row>
                    <Row>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Giá tour</Form.Label><Form.Control required min="1" type="number" value={serviceForm.price} onChange={(e) => updateService('price', e.target.value)} /></Form.Group></Col>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Số chỗ còn trống</Form.Label><Form.Control required min="0" type="number" value={serviceForm.availableSlots} onChange={(e) => updateService('availableSlots', e.target.value)} /></Form.Group></Col>
                    </Row>
                    {imageInput(!editingService)}
                    <Row>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Điểm khởi hành</Form.Label><Form.Select required value={serviceForm.departureLocationId} onChange={(e) => updateService('departureLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Điểm đến</Form.Label><Form.Select required value={serviceForm.destinationLocationId} onChange={(e) => updateService('destinationLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                    </Row>
                </>
            );
        }
        return (
            <>
                {businessType === 'AIRLINE' && (
                    <>
                        <Form.Group className="mb-3"><Form.Label>Mã chuyến bay</Form.Label><Form.Control required value={serviceForm.flightCode} onChange={(e) => updateService('flightCode', e.target.value)} /></Form.Group>
                        {imageInput(!editingService)}
                    </>
                )}
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Thời gian khởi hành</Form.Label><Form.Control required type="datetime-local" value={serviceForm.departureTime} onChange={(e) => updateService('departureTime', e.target.value)} /></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Thời gian đến</Form.Label><Form.Control required type="datetime-local" value={serviceForm.arrivalTime} onChange={(e) => updateService('arrivalTime', e.target.value)} /></Form.Group></Col>
                </Row>
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Giá vé</Form.Label><Form.Control required min="1" type="number" value={serviceForm.price} onChange={(e) => updateService('price', e.target.value)} /></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Số ghế còn trống</Form.Label><Form.Control required min="0" type="number" value={serviceForm.availableSeats} onChange={(e) => updateService('availableSeats', e.target.value)} /></Form.Group></Col>
                </Row>
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Điểm khởi hành</Form.Label><Form.Select required value={serviceForm.departureLocationId} onChange={(e) => updateService('departureLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Điểm đến</Form.Label><Form.Select required value={serviceForm.arrivalLocationId} onChange={(e) => updateService('arrivalLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                </Row>
            </>
        );
    };

    if (!user || user.role !== 'PROVIDER') {
        return (
            <div className="container mt-5 text-center">
                <Card className="p-4 border-danger">
                    <h4 className="text-danger">Không có quyền truy cập!</h4>
                    <p className="text-muted">Vui lòng đăng nhập tài khoản nhà cung cấp.</p>
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
                        <Nav.Link onClick={() => setActiveTab('overview')} className={`text-white mb-3 ${activeTab === 'overview' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Tổng quan</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('services')} className={`text-white mb-3 ${activeTab === 'services' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Quản lý dịch vụ</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('bookings')} className={`text-white mb-3 ${activeTab === 'bookings' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Đơn đặt hàng</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('stats')} className={`text-white mb-3 ${activeTab === 'stats' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Thống kê</Nav.Link>
                    </Nav>
                </div>
                <div><hr className="bg-secondary" /><Button variant="danger" className="w-100 fw-bold" onClick={logout}>Đăng xuất</Button></div>
            </Col>

            <Col md={9} className="p-4 bg-light">
                {error && <Alert variant="danger">{error}</Alert>}
                {activeTab === 'overview' && (
                    <div>
                        <h3 className="mb-2 fw-bold">Tổng quan hệ thống</h3>
                        <p className="text-muted mb-4">Loại hình: <strong>{labels[businessType] || 'Đang tải...'}</strong></p>
                        <Row>
                            <Col md={6} className="mb-3"><Card className="p-3 shadow-sm border-0 bg-white"><Card.Body><h6 className="text-muted">TỔNG DOANH THU</h6><h3 className="text-primary fw-bold">{stats.revenue?.toLocaleString() || 0} VNĐ</h3></Card.Body></Card></Col>
                            <Col md={6} className="mb-3"><Card className="p-3 shadow-sm border-0 bg-white"><Card.Body><h6 className="text-muted">TỔNG ĐƠN ĐẶT DỊCH VỤ</h6><h3 className="text-success fw-bold">{stats.bookings || 0} đơn</h3></Card.Body></Card></Col>
                        </Row>
                    </div>
                )}

                {activeTab === 'services' && (
                    <div>
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <div>
                                <h4 className="fw-bold mb-1">Danh sách dịch vụ</h4>
                                <span className="text-muted">Loại hình: {labels[businessType] || 'Đang tải...'}</span>
                            </div>
                            <Button variant="success" disabled={!businessType} onClick={openAddService}>+ Thêm dịch vụ</Button>
                        </div>
                        <Card className="border-0 shadow-sm">
                            <Table responsive hover className="m-0 align-middle">
                                <thead className="table-secondary">
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên dịch vụ</th>
                                        <th>Loại hình</th>
                                        {!isHotel && <th>Giá</th>}
                                        <th className="text-center">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {services.length > 0 ? services.map((service) => (
                                        <tr key={service.id}>
                                            <td>#{service.id}</td>
                                            <td className="fw-bold">{serviceName(service, businessType)}</td>
                                            <td><span className="badge bg-info text-dark">{labels[businessType]}</span></td>
                                            {!isHotel && <td className="text-primary fw-bold">{Number(service.price || 0).toLocaleString()} VNĐ</td>}
                                            <td className="text-center">
                                                {isHotel && <Button variant="success" size="sm" className="me-2" onClick={() => openRoomModal(service)}>Thêm phòng</Button>}
                                                <Button variant="warning" size="sm" className="me-2" onClick={() => openEditService(service)}>Sửa</Button>
                                                <Button variant="danger" size="sm" onClick={() => deleteService(service.id)}>Xóa</Button>
                                            </td>
                                        </tr>
                                    )) : (
                                        <tr><td colSpan={isHotel ? 4 : 5} className="text-center p-4 text-muted">Chưa có dịch vụ nào.</td></tr>
                                    )}
                                </tbody>
                            </Table>
                        </Card>
                    </div>
                )}

                {activeTab === 'bookings' && <div><h4 className="fw-bold mb-3">Danh sách đơn đặt hàng</h4><Card className="border-0 shadow-sm p-4 text-center text-muted">Tính năng quản lý đơn hàng đang được đồng bộ...</Card></div>}

                {activeTab === 'stats' && (
    <div>
        <h3 className="mb-4 fw-bold text-dark">Báo cáo & Thống kê doanh thu</h3>
        
        {/* Hàng hiển thị số tổng quan bốc trực tiếp từ cục stats của API BE */}
        <Row className="mb-4">
            <Col md={6} className="mb-3">
                <Card className="p-3 shadow-sm border-0 bg-white">
                    <Card.Body>
                        <h6 className="text-muted text-uppercase fw-semibold small">Tổng số lượt đặt dịch vụ</h6>
                        <h3 className="text-info fw-bold mt-2">
                            {stats.bookings || 0} <span className="fs-6 fw-normal text-muted">lượt</span>
                        </h3>
                    </Card.Body>
                </Card>
            </Col>
            <Col md={6} className="mb-3">
                <Card className="p-3 shadow-sm border-0 bg-white">
                    <Card.Body>
                        <h6 className="text-muted text-uppercase fw-semibold small">Tổng doanh thu</h6>
                        <h3 className="text-primary fw-bold mt-2">
                            {(stats.revenue || 0).toLocaleString()} <span className="fs-6 fw-normal text-muted">VNĐ</span>
                        </h3>
                    </Card.Body>
                </Card>
            </Col>
        </Row>

        {/* Khối biểu đồ cột thông minh kết nối mảng dữ liệu đơn hàng từ BE */}
        <Card className="shadow-sm border-0 bg-white p-4 mb-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="fw-bold m-0 text-secondary">
                    {timeType === 'MONTH' ? 'Biểu đồ doanh thu theo các tháng' : 'Biểu đồ doanh thu qua các năm'}
                </h5>
                <Form.Select 
                    style={{ width: '220px' }} 
                    value={timeType} 
                    onChange={(e) => setTimeType(e.target.value)}
                    className="form-select-sm shadow-none"
                >
                    <option value="MONTH">Thống kê theo Tháng</option>
                    <option value="YEAR">Thống kê theo Năm</option>
                </Form.Select>
            </div>

            <div style={{ width: '100%', height: 320 }}>
                <ResponsiveContainer>
                    <BarChart
                        data={timeType === 'MONTH' ? monthlyData : yearlyData}
                        margin={{ top: 10, right: 30, left: 20, bottom: 5 }}
                    >
                        <CartesianGrid strokeDasharray="3 3" vertical={false} />
                        <XAxis dataKey={timeType === 'MONTH' ? "month" : "year"} tickFormatter={(v) => timeType === 'MONTH' ? `Tháng ${v}` : `Năm ${v}`} />
                        <YAxis />
                        <Tooltip formatter={(value) => [Number(value).toLocaleString() + " VNĐ", "Doanh thu"]} />
                        <Legend />
                        <Bar dataKey="revenue" name="Doanh thu" fill="#0d6efd" radius={[4, 4, 0, 0]} />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </Card>
    </div>
)}
            </Col>

            <Modal show={showServiceModal} onHide={() => { setShowServiceModal(false); resetServiceModal(); }} centered size="lg">
                <Modal.Header closeButton><Modal.Title className="fw-bold">{editingService ? 'Sửa dịch vụ' : 'Thêm dịch vụ mới'}</Modal.Title></Modal.Header>
                <Form onSubmit={saveService}>
                    <Modal.Body>
                        {serviceFields()}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => { setShowServiceModal(false); resetServiceModal(); }}>Hủy</Button>
                        <Button type="submit" variant="success">Lưu lại</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            <Modal show={showRoomModal} onHide={() => { setShowRoomModal(false); resetRoomModal(); }} centered size="lg">
                <Modal.Header closeButton><Modal.Title className="fw-bold">Thêm phòng mới</Modal.Title></Modal.Header>
                <Form onSubmit={saveRoom}>
                    <Modal.Body>{roomFields}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => { setShowRoomModal(false); resetRoomModal(); }}>Hủy</Button>
                        <Button type="submit" variant="success">Lưu phòng</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Row>
    );
};

export default ProviderDashboard;
