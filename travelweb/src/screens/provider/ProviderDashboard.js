// src/screens/provider/ProviderDashboard.js
import React, { useState, useEffect, useContext } from 'react';
import { Row, Col, Card, Nav, Table, Button, Modal, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import cookies from 'react-cookies';
import { MyUserContext, MyDispatchContext } from '../../configs/MyContext';
import Apis, { authApis } from "../../configs/Apis";

const ProviderDashboard = () => {
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatchContext);
    const navigate = useNavigate();
    
    const [activeTab, setActiveTab] = useState('overview');
    
    // State cho Thống kê
    const [stats, setStats] = useState({ revenue: 0, bookings: 0 });
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');

    // State cho Quản lý dịch vụ
    const [services, setServices] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [newService, setNewService] = useState({ name: '', type: 'HOTEL', price: '' });

    // Hàm đăng xuất gọn gàng giống bên Admin của BE
    const handleLogout = () => {
        if (window.confirm("Bạn có chắc chắn muốn đăng xuất?")) {
            cookies.remove('token', { path: '/' });
            cookies.remove('user', { path: '/' });
            dispatch({ type: 'logout' });
            navigate('/user');
        }
    };

    // Gọi API thống kê và dịch vụ
    const fetchStats = async () => {
        if (!user) return;
        try {
            const res = await authApis().get('/provider/statistics', {
                params: { providerId: user.id, fromDate, toDate }
            });
            setStats(res.data);
        } catch (err) {
            console.error("Lỗi lấy thống kê:", err);
        }
    };

    const fetchServices = async () => {
        if (!user) return;
        try {
            const res = await authApis().get('/provider/services', {
                params: { providerId: user.id }
            });
            setServices(res.data);
        } catch (err) {
            console.error("Lỗi lấy dịch vụ:", err);
        }
    };

    useEffect(() => {
        if (user) {
            fetchStats();
            fetchServices();
        }
    }, [user]);

    const handleAddService = async (e) => {
    if (e && e.preventDefault) e.preventDefault();
    try {
        // Đảm bảo trường giá tiền luôn là chuỗi số hợp lệ, không được để trống ô nhập
        const safePrice = newService.price && String(newService.price).trim() !== "" ? String(newService.price) : "0";

        // Gom toàn bộ trường mà các hàm build bên BE cũ bắt buộc phải bóc từ Map ra
        const payload = {
            // 3 trường chính từ Form của bạn
            name: newService.name || "",
            type: newService.type || "HOTEL",
            price: safePrice,
            providerId: String(user.id),

            // --- CỨU LỖI 500: Nhồi các trường ẩn để hàm buildHotel/buildTour ở BE không bị null ---
            // Dành cho thực thể HOTEL (hàm buildHotel)
            hotelName: newService.name || "", 
            description: "Mô tả dịch vụ mặc định",
            address: "Địa chỉ mặc định",
            thumbnail: "",
            locationId: "1", // BẮT BUỘC: để BE gọi Long.valueOf("1") thành công

            // Dành cho thực thể TOUR (hàm buildTour)
            title: newService.name || "",
            departureDate: new Date().toISOString().slice(0, 16), // Định dạng YYYY-MM-DDTHH:mm
            durationDays: "1",      // BẮT BUỘC: để BE gọi Integer.parseInt("1")
            availableSlots: "10",   // BẮT BUỘC: để BE gọi Integer.parseInt("10")
            departureLocationId: "1",
            destinationLocationId: "1",

            // Dành cho thực thể FLIGHT & BUS (hàm buildFlight / buildBusTrip)
            flightCode: "FLIGHT-001",
            departureTime: new Date().toISOString().slice(0, 16),
            arrivalTime: new Date().toISOString().slice(0, 16),
            arrivalLocationId: "1",
            availableSeats: "30"    // BẮT BUỘC: để BE gọi Integer.parseInt("30")
        };

        // Gửi bằng JSON thuần túy (đúng với @RequestBody gốc của BE)
        await authApis().post('/provider/services', payload, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        setShowModal(false);
        setNewService({ name: '', type: 'HOTEL', price: '' });
        fetchServices();
        alert("Thêm dịch vụ thành công!");
    } catch (err) {
        console.log("Chi tiết lỗi:", err.response?.data);
        alert("Lỗi khi thêm dịch vụ!");
    }
};

    const handleUpdateService = async (id, updatedData) => {
    try {
        const safePrice = updatedData.price && String(updatedData.price).trim() !== "" ? String(updatedData.price) : "0";

        const payload = {
            name: updatedData.name || "",
            type: updatedData.type || "HOTEL",
            price: safePrice,
            providerId: String(user.id),

            // Nhồi đầy đủ các trường tương tự như hàm thêm để tránh lỗi bên BE cũ
            hotelName: updatedData.name || "", 
            description: "Mô tả cập nhật",
            address: "Địa chỉ cập nhật",
            thumbnail: "",
            locationId: "1", 

            title: updatedData.name || "",
            departureDate: new Date().toISOString().slice(0, 16),
            durationDays: "1",
            availableSlots: "10",
            departureLocationId: "1",
            destinationLocationId: "1",

            flightCode: "FLIGHT-001",
            departureTime: new Date().toISOString().slice(0, 16),
            arrivalTime: new Date().toISOString().slice(0, 16),
            arrivalLocationId: "1",
            availableSeats: "30"
        };

        // Gọi API PUT bằng JSON thuần đúng theo Controller gốc
        await authApis().put(`/provider/services/${id}`, payload, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        alert("Cập nhật dịch vụ thành công!");
        fetchServices();
    } catch (err) {
        console.log("Lỗi khi sửa:", err.response?.data);
        alert("Lỗi khi cập nhật dịch vụ!");
    }
};

    const handleDeleteService = async (id) => {
        if (window.confirm("Bạn có chắc chắn muốn xóa?")) {
            try {
                await authApis().delete(`/provider/services/${id}`, { params: { providerId: user.id } });
                fetchServices();
                alert("Xóa thành công!");
            } catch (err) {
                alert("Lỗi khi xóa!");
            }
        }
    };

    if (!user || user.role !== 'PROVIDER') {
        return (
            <div className="container mt-5 text-center">
                <Card className="p-4 border-danger">
                    <h4 className="text-danger">Không có quyền truy cập!</h4>
                    <p className="text-muted">Vui lòng đăng nhập tài khoản Nhà cung cấp.</p>
                </Card>
            </div>
        );
    }

    return (
        <Row className="w-100 m-0" style={{ minHeight: '100vh' }}>
            {/* SIDEBAR GỐC - THÊM NÚT ĐĂNG XUẤT Ở DƯỚI CÙNG */}
            <Col md={3} className="bg-dark text-white p-4 d-flex flex-column justify-content-between">
                <div>
                    <h1 className="text-center mb-8">Travel Provider</h1>
                    <hr className="bg-secondary" />
                    
                    <Nav className="flex-column">
                        <Nav.Link onClick={() => setActiveTab('overview')} className={`text-white mb-3 custom-link ${activeTab === 'overview' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Tổng quan</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('services')} className={`text-white mb-3 custom-link ${activeTab === 'services' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Quản lý dịch vụ</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('bookings')} className={`text-white mb-3 custom-link ${activeTab === 'bookings' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Đơn đặt hàng</Nav.Link>
                        <Nav.Link onClick={() => setActiveTab('stats')} className={`text-white mb-3 custom-link ${activeTab === 'stats' ? 'fw-bold text-warning' : ''}`} style={{ cursor: 'pointer' }}>Thống kê</Nav.Link>
                    </Nav>
                </div>

                {/* KHU VỰC NÚT ĐĂNG XUẤT */}
                <div>
                    <hr className="bg-secondary" />
                    <Button variant="danger" className="w-100 fw-bold" onClick={handleLogout}>
                        Đăng xuất
                    </Button>
                </div>
            </Col>

            {/* MAIN CONTENT BÊN PHẢI */}
            <Col md={9} className="p-4 bg-light">
                {/* TAB 1: TỔNG QUAN */}
                {activeTab === 'overview' && (
                    <div>
                        <h3 className="mb-4 fw-bold">Tổng quan hệ thống</h3>
                        <Row>
                            <Col md={6} className="mb-3">
                                <Card className="p-3 shadow-sm border-0 bg-white">
                                    <Card.Body>
                                        <h6 className="text-muted">TỔNG DOANH THU</h6>
                                        <h3 className="text-primary fw-bold">{stats.revenue?.toLocaleString() || 0} VNĐ</h3>
                                    </Card.Body>
                                </Card>
                            </Col>
                            <Col md={6} className="mb-3">
                                <Card className="p-3 shadow-sm border-0 bg-white">
                                    <Card.Body>
                                        <h6 className="text-muted">TỔNG LƯỢNG ĐẶT DỊCH VỤ</h6>
                                        <h3 className="text-success fw-bold">{stats.bookings || 0} đơn</h3>
                                    </Card.Body>
                                </Card>
                            </Col>
                        </Row>
                    </div>
                )}

                {/* TAB 2: QUẢN LÝ DỊCH VỤ */}
                {activeTab === 'services' && (
                    <div>
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h4 className="fw-bold">Danh sách dịch vụ</h4>
                            <Button variant="success" onClick={() => setShowModal(true)}>+ Thêm dịch vụ</Button>
                        </div>
                        <Card className="border-0 shadow-sm">
                            <Table responsive hover className="m-0 align-middle">
                                <thead className="table-secondary">
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên dịch vụ</th>
                                        <th>Loại hình</th>
                                        <th>Giá</th>
                                        <th className="text-center">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {services.length > 0 ? services.map(s => (
                                        <tr key={s.id}>
                                            <td>#{s.id}</td>
                                            <td className="fw-bold">{s.name}</td>
                                            <td><span className="badge bg-info text-dark">{s.type}</span></td>
                                            <td className="text-primary fw-bold">{Number(s.price)?.toLocaleString()} VNĐ</td>
                                            <td className="text-center">
                                                <Button variant="danger" size="sm" onClick={() => handleDeleteService(s.id)}>Xóa</Button>
                                            </td>
                                        </tr>
                                    )) : (
                                        <tr><td colSpan="5" className="text-center p-4 text-muted">Chưa có dịch vụ nào.</td></tr>
                                    )}
                                </tbody>
                            </Table>
                        </Card>
                    </div>
                )}

                {/* TAB 3: ĐƠN ĐẶT HÀNG */}
                {activeTab === 'bookings' && (
                    <div>
                        <h4 className="fw-bold mb-3">Danh sách đơn đặt hàng</h4>
                        <Card className="border-0 shadow-sm p-4 text-center text-muted">
                            Tính năng quản lý đơn hàng đang được đồng bộ...
                        </Card>
                    </div>
                )}

                {/* TAB 4: THỐNG KÊ */}
                {activeTab === 'stats' && (
                    <div>
                        <h4 className="fw-bold mb-3">Lọc thống kê</h4>
                        <Card className="p-3 border-0 shadow-sm mb-3">
                            <Form onSubmit={(e) => { e.preventDefault(); fetchStats(); }}>
                                <Row className="align-items-end">
                                    <Col md={4} className="mb-2">
                                        <Form.Label className="small text-muted">Từ ngày</Form.Label>
                                        <Form.Control type="date" value={fromDate} onChange={e => setFromDate(e.target.value)} />
                                    </Col>
                                    <Col md={4} className="mb-2">
                                        <Form.Label className="small text-muted">Đến ngày</Form.Label>
                                        <Form.Control type="date" value={toDate} onChange={e => setToDate(e.target.value)} />
                                    </Col>
                                    <Col md={4} className="mb-2">
                                        <Button type="submit" variant="primary" className="w-100">Lọc kết quả</Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card>
                    </div>
                )}
            </Col>

            {/* MODAL THÊM DỊCH VỤ GỌN GÀNG */}
            <Modal show={showModal} onHide={() => setShowModal(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title className="fw-bold">Thêm dịch vụ mới</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleAddService}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên dịch vụ</Form.Label>
                            <Form.Control type="text" required value={newService.name} onChange={e => setNewService({...newService, name: e.target.value})} placeholder="Nhập tên dịch vụ..." />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Loại hình</Form.Label>
                            <Form.Select value={newService.type} onChange={e => setNewService({...newService, type: e.target.value})}>
                                <option value="HOTEL">Khách Sạn</option>
                                <option value="TOUR">Tour Du Lịch</option>
                                <option value="FLIGHT">Chuyến Bay</option>
                                <option value="BUS">Xe Khách</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Giá bán (VNĐ)</Form.Label>
                            <Form.Control type="number" required value={newService.price} onChange={e => setNewService({...newService, price: e.target.value})} placeholder="Nhập giá..." />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
                        <Button type="submit" variant="success">Lưu lại</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Row>
    );
};

export default ProviderDashboard;