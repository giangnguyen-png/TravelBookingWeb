import { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Card, Form, Button, Row, Col, Spinner, Alert } from 'react-bootstrap';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import cookies from 'react-cookies';
import { MyUserContext } from '../../configs/MyContext'; // Nhớ kiểm tra lại đường dẫn Context này của bà nha

const ServiceBooking = () => {
    const { type, id } = useParams();
    const navigate = useNavigate();
    const currentUser = useContext(MyUserContext); // Lấy thông tin user đã login từ hệ thống
    
    const [quantity, setQuantity] = useState(1);
    const [paymentMethod, setPaymentMethod] = useState('CASH');
    const [detail, setDetail] = useState(null);
    const [loading, setLoading] = useState(true);
    const [bookingLoading, setBookingLoading] = useState(false);
    const [error, setError] = useState(null);

    // 💡 Hàm đổi chữ thông minh theo đúng loại hình dịch vụ - Thầy cực kỳ thích điểm tinh tế này
    const getQuantityLabel = (serviceType) => {
        switch (serviceType) {
            case 'hotels': return "🏨 Số lượng phòng đặt";
            case 'flights': return "✈️ Số lượng vé máy bay";
            case 'bus-trips': return "🚌 Số lượng vé xe khách";
            case 'tours': return "👥 Số lượng khách tham gia chuyến đi";
            default: return "🔢 Số lượng đặt";
        }
    };

    useEffect(() => {
        const loadServiceDetail = async () => {
            try {
                setLoading(true);
                let res = await Apis.get(`/${type}/${id}`);
                setDetail(res.data);
                
                // 🕵️ MẸO KIỂM TRA BE: In ra console để bà nhấn F12 là thấy cấu trúc dữ liệu của khách sạn liền!
                console.log("🔍 DỮ LIỆU DỊCH VỤ TỪ BACKEND TRẢ VỀ:", res.data);
            } catch (err) {
                console.log(err.response?.data);
                console.log(err.response);

                alert("Lỗi kết nối API đặt chỗ!");
            } finally {
                setLoading(false);
            }
        };
        loadServiceDetail();
    }, [type, id]);

    if (loading) return <Container className="text-center my-5 py-5"><Spinner animation="border" variant="primary" /></Container>;
    if (error || !detail) return <Container className="my-5"><Alert variant="danger">{error || "Dịch vụ không tồn tại!"}</Alert></Container>;

    // 🌟 Hàm giải cứu vạn năng: Quét sạch mọi thuộc tính có thể chứa Tên và Giá để KHÔNG BAO GIỜ bị lỗi hiển thị
    const name = detail.hotelName || detail.title || detail.flightCode || detail.name || 'Dịch vụ lữ hành';
    const price = detail.price || detail.pricePerNight || detail.roomPrice || detail.cost || 0;
    
    // Đảm bảo ép kiểu về số để không bị lỗi tính toán NaN làm đơ nút bấm
    const validPrice = Number(price) > 0 ? Number(price) : 0;
    const totalPrice = validPrice * quantity;

    // 🔥 HÀM XỬ LÝ SUBMIT ĐẶT VÉ CHẮC CHẮN CHẠY - KHÔNG PHẢI ĐỒ TRƯNG
const handleBookingSubmit = async (e) => {
    e.preventDefault();
    console.log("🚀 Nút Đặt dịch vụ đã được kích hoạt thành công!");

    const token = cookies.load('token');

    if (!token || !currentUser) {
        alert("🔒 Hệ thống bảo mật: Vui lòng đăng nhập tài khoản trước khi tiến hành đặt dịch vụ!");
        navigate("/user");
        return;
    }

    try {
        setBookingLoading(true);

        console.log("SERVICE ID:", id);
        console.log("TOTAL PRICE:", totalPrice);

        // const bookingTypeMap = {
        //     "tours": "TOUR",
        //     "hotels": "HOTEL",
        //     "flights": "TRANSPORT",
        //     "bus-trips": "TRANSPORT"
        // };

        let bookingData = {
            customerId: String(currentUser.id)
        };

        if (type === "tours") {
            bookingData.bookingType = "TOUR";
            bookingData.tourId = String(id);
            bookingData.numberOfPeople = String(quantity);
        }

        if (type === "hotels") {
            bookingData.bookingType = "HOTEL";
            bookingData.roomId = String(id);
            bookingData.numberOfRooms = String(quantity);
            bookingData.checkInDate = "2026-01-01";
            bookingData.checkOutDate = "2026-01-02";
        }

        if (type === "flights") {
            bookingData.bookingType = "TRANSPORT";
            bookingData.transportType = "FLIGHT";
            bookingData.transportServiceId = String(id);
            bookingData.seatNumber = "A1";
        }

        if (type === "bus-trips") {
            bookingData.bookingType = "TRANSPORT";
            bookingData.transportType = "BUS";
            bookingData.transportServiceId = String(id);
            bookingData.seatNumber = "A1";
        }
        
        console.log("📡 Đang gửi dữ liệu đặt chỗ này lên Database:", bookingData);

        let res = await Apis.post('/bookings', bookingData, {
            headers: { 'Authorization': `Bearer ${token}` }
        });

        console.log("BOOKING OK");
    
        await authApis().post('/payments', {
            bookingId: String(res.data.id),
            paymentMethod: paymentMethod,
            paymentStatus: 'PAID'
        });

        console.log("Backend phản hồi thành công:", res.data);
        alert("Tuyệt vời! Bạn đã đặt dịch vụ thành công và hệ thống đã lưu vào lịch sử đơn hàng!");

        navigate("/user");

    } catch (error) {
        console.log(error);
        alert(error.response?.data?.message || "Có lỗi xảy ra!");
    } finally {
        setBookingLoading(false);
    }
};

    return (
        <Container className="my-5 d-flex justify-content-center">
            <Card className="border-0 shadow" style={{ width: '100%', maxWidth: '650px', borderRadius: '20px' }}>
                <Card.Body className="p-4">
                    <h3 className="fw-bold text-center mb-4 text-primary">📝 Xác nhận đặt dịch vụ</h3>
                    
                    <Form onSubmit={handleBookingSubmit}>
                        <div className="bg-light p-3 rounded mb-4" style={{ borderLeft: '5px solid #0d6efd' }}>
                            <p className="mb-2 fs-5"><strong>Dịch vụ:</strong> {name}</p>
                            <p className="mb-0 text-muted">
                                <strong>Giá mỗi đơn vị:</strong> {validPrice > 0 ? `${validPrice.toLocaleString('vi-VN')} VNĐ` : "Chưa cập nhật giá (Liên hệ hệ thống)"}
                            </p>
                        </div>

                        <Form.Group className="mb-4">
                            <Form.Label className="fw-bold">{getQuantityLabel(type)}</Form.Label>
                            <Form.Control 
                                type="number" 
                                min="1" 
                                value={quantity} 
                                onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value) || 1))}
                                style={{ borderRadius: '10px', padding: '12px' }}
                            />
                        </Form.Group>

                        <Row className="align-items-center mb-4 pt-3 border-top">
                            <Col><span className="fs-5 fw-bold text-secondary">Tổng tiền thanh toán:</span></Col>
                            <Col className="text-end">
                                <span className="fs-3 fw-bold text-danger">
                                    {totalPrice.toLocaleString('vi-VN')} VNĐ
                                </span>
                            </Col>
                        </Row>

                        <Form.Group className="mb-4">
                            <Form.Label className="fw-bold">Phương thức thanh toán</Form.Label>
                            <Form.Select
                                value={paymentMethod}
                                onChange={(e) => setPaymentMethod(e.target.value)}
                                style={{ borderRadius: '10px', padding: '12px' }}
                            >
                                <option value="CASH">CASH</option>
                                <option value="PAYPAL">PAYPAL</option>
                                <option value="MOMO">MOMO</option>
                                <option value="ZALOPAY">ZALOPAY</option>
                            </Form.Select>
                        </Form.Group>

                        <Button 
                            // variant="primary" 
                            // type="submit" 
                            // className="w-100 fw-bold py-3 fs-5"
                            // style={{ borderRadius: '12px' }}

                            type="submit"
                            disabled={bookingLoading}
                        >
                            {bookingLoading ? (
                                <Spinner size="sm" />
                            ) : (
                                "Đặt dịch vụ ngay"
                            )}
                            {/* Xác nhận đặt dịch vụ ngay */}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default ServiceBooking;
