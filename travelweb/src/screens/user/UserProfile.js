import { useContext, useEffect, useState } from "react";
import { Container, Card, Row, Col, Badge, Spinner } from "react-bootstrap";
import { authApis } from "../../configs/Apis";
import { MyUserContext } from "../../configs/MyContext";
import { useNavigate } from "react-router-dom";

const UserProfile = () => {
    const currentUser = useContext(MyUserContext);
    const navigate = useNavigate();
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (!currentUser) {
            navigate("/login");
            return;
        }

        const fetchHistory = async () => {
            try {
                //Gọi đúng API của Backend: /bookings/me?customerId=...
                let res = await authApis().get(`/bookings/me?customerId=${currentUser.id}`);
                setHistory(res.data);
                setLoading(false);
            } catch (err) {
                console.error("Lỗi tải lịch sử đặt vé:", err);
            } finally {
                setLoading(false);
            }
        };
        fetchHistory();
    }, [currentUser, navigate]);

    if (!currentUser) return null;

    return (
        <Container className="my-5">
            <h2 className="fw-bold mb-4 text-primary">👤 Trang Cá Nhân</h2>
            <Row>
                {/* CỘT THÔNG TIN CÁ NHÂN */}
                <Col md={4} className="mb-4">
                    <Card className="shadow-sm border-0 text-center p-4" style={{ borderRadius: '15px' }}>
                        <Card.Img 
                            variant="top" 
                            src={currentUser.avatar || 'https://via.placeholder.com/150'} 
                            className="rounded-circle mx-auto mb-3"
                            style={{ width: '120px', height: '120px', objectFit: 'cover' }}
                        />
                        <Card.Title className="fw-bold fs-4">{currentUser.fullName}</Card.Title>
                        <Card.Text className="text-muted">{currentUser.email}</Card.Text>
                        <Badge bg="success" className="p-2 fs-6">{currentUser.role}</Badge>
                    </Card>
                </Col>

                {/* CỘT LỊCH SỬ ĐẶT VÉ */}
                <Col md={8}>
                    <Card className="shadow-sm border-0 p-4" style={{ borderRadius: '15px' }}>
                        <h4 className="fw-bold mb-4">🛒 Lịch sử đặt dịch vụ</h4>
                        {loading ? (
                            <Spinner animation="border" variant="primary" />
                        ) : history.length > 0 ? (
                            history.map(b => (
                                <div key={b.id} className="p-3 mb-3 bg-light rounded border-start border-primary border-5">
                                    <h6 className="fw-bold text-uppercase mb-1">Mã đơn: #{b.id} - Dịch vụ: {b.bookingType}</h6>
                                    <p className="mb-1">Tổng tiền: <span className="text-danger fw-bold">{Number(b.totalPrice).toLocaleString('vi-VN')} VNĐ</span></p>
                                    <p className="mb-0">Trạng thái: <Badge bg={b.status === 'CONFIRMED' ? 'success' : 'warning'}>{b.status}</Badge></p>
                                </div>
                            ))
                        ) : (
                            <p className="text-muted">Bạn chưa đặt dịch vụ nào cả. Hãy ra trang chủ tìm kiếm nhé!</p>
                        )}
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};
export default UserProfile;