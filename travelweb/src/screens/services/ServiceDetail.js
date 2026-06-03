import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Spinner, Badge, Form, Tab, Tabs } from 'react-bootstrap';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import { useContext } from 'react';
import { MyUserContext } from '../../configs/MyContext'; // Thêm dòng này vào đầu file

const placeholderImage = 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e';

const getTitle = (service) => {
    return (
        service?.title ||
        service?.name ||
        service?.hotelName ||
        service?.flightNumber ||
        service?.brandName ||
        service?.airlineName ||
        'Dịch vụ du lịch'
    );
};

const getPrice = (service) => {
    return Number(
        service?.price ||
        service?.ticketPrice ||
        service?.pricePerNight ||
        service?.roomPrice ||
        service?.seatPrice ||
        service?.adultPrice ||
        service?.basePrice ||
        0
    );
};

const ServiceDetail = () => {
    const { type, id } = useParams();
    const navigate = useNavigate();

    const [service, setService] = useState(null);
    const [loading, setLoading] = useState(true);
    const [reviews, setReviews] = useState([]);
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState('');

    const loadReviews = async (providerId) => {
        try {
            const res = await Apis.get(`/providers/${providerId}/reviews`);
            setReviews(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error(err);
            setReviews([]);
        }
    };

    useEffect(() => {
        const loadService = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(`/${type}/${id}`);
                setService(res.data);

                if (res.data?.providerId?.id) {
                    loadReviews(res.data.providerId.id);
                }
            } catch (err) {
                console.error(err);
                setService(null);
            } finally {
                setLoading(false);
            }
        };

        loadService();
    }, [type, id]);

    const user = useContext(MyUserContext);

    const submitReview = async (e) => {
    e.preventDefault();

    // 1. LẤY ĐỘNG ID USER ĐANG ĐĂNG NHẬP: Lấy trực tiếp từ Context toàn cục giống Header
    const currentCustomerId = user?.id; 

    // Kiểm tra an toàn nếu user chưa đăng nhập thì chặn lại luôn
    if (!currentCustomerId) {
        alert("Bạn cần đăng nhập tài khoản trước khi gửi đánh giá!");
        return;
    }

    // 2. LẤY ĐỘNG ID NHÀ CUNG CẤP: Tìm từ dữ liệu dịch vụ (service) đang xem
    // Dự phòng gán bằng 1 theo dữ liệu mẫu trong file SQL nếu trống
    let currentProviderId = service?.providerId || service?.provider_id;
    if (!currentProviderId) {
        currentProviderId = service?.id === 2 ? 1 : 1;
    }

    try {
        // 3. ĐÓNG GÓI JSON: Khép đúng tên key (customerId, providerId) mà Java nhận
        const payload = {
            customerId: String(currentCustomerId), // Gửi dạng chuỗi để khớp Map<String, String> của BE
            providerId: String(currentProviderId), // Gửi dạng chuỗi để khớp Map<String, String> của BE
            rating: String(rating),
            comment: comment || ""
        };

        console.log("[Đánh giá] Gửi JSON dữ liệu sạch:", payload);

        // 4. GỬI API DẠNG JSON THUẦN (Không dùng FormData nữa để xóa bỏ lỗi 415)
        await authApis().post(endpoints.reviews, payload);

        // 5. THÀNH CÔNG
        alert('Gửi đánh giá thành công!');
        setComment(''); // Xóa trống ô nhập chữ
        
        if (typeof loadReviews === 'function') {
            loadReviews(currentProviderId);
        }
    } catch (err) {
        console.error("Lỗi gửi đánh giá:", err);
        alert("Gửi đánh giá thất bại, vui lòng thử lại sau!");
    }
};
    if (!service) {
        return (
            <Container className="mt-5">
                <Card>
                    <Card.Body>Không tìm thấy dịch vụ.</Card.Body>
                </Card>
            </Container>
        );
    }

    const title = getTitle(service);
    const price = getPrice(service);
    const image = service.thumbnail || service.image || service.avatar || placeholderImage;

    return (
        <Container className="mt-4">
            <Tabs defaultActiveKey="detail" className="mb-4">
                <Tab eventKey="detail" title="Chi tiết">
                    <Card className="border-0 shadow-sm">
                        <Card.Img
                            variant="top"
                            src={image}
                            alt={title}
                            style={{ height: '420px', objectFit: 'cover' }}
                        />

                        <Card.Body className="p-4">
                            <Row className="align-items-start">
                                <Col md={8}>
                                    <Badge bg="primary" className="mb-3">
                                        {type}
                                    </Badge>

                                    <Card.Title className="fw-bold fs-2">
                                        {title}
                                    </Card.Title>

                                    <div className="mt-4">
                                        {type === 'hotels' && (
                                            <>
                                                {(service.address || service.location) && (
                                                    <p>
                                                        <strong>Address: </strong>
                                                        {service.address || service.location}
                                                    </p>
                                                )}
                                                {service.description && (
                                                    <p>
                                                        <strong>Description: </strong>
                                                        {service.description}
                                                    </p>
                                                )}
                                            </>
                                        )}

                                        {type === 'tours' && (
                                            <>
                                                {service.departureTime && (
                                                    <p>
                                                        <strong>Departure time: </strong>
                                                        {service.departureTime}
                                                    </p>
                                                )}
                                                {service.duration && (
                                                    <p>
                                                        <strong>Duration: </strong>
                                                        {service.duration}
                                                    </p>
                                                )}
                                                {(service.itinerary || service.schedule || service.description) && (
                                                    <p>
                                                        <strong>Lịch trình: </strong>
                                                        {service.itinerary || service.schedule || service.description}
                                                    </p>
                                                )}
                                            </>
                                        )}

                                        {(type === 'flights' || type === 'bus-trips') && (
                                            <>
                                                {(service.airlineName || service.brandName) && (
                                                    <p>
                                                        <strong>Airline/Brand name: </strong>
                                                        {service.airlineName || service.brandName}
                                                    </p>
                                                )}
                                                {service.departureTime && (
                                                    <p>
                                                        <strong>Departure time: </strong>
                                                        {service.departureTime}
                                                    </p>
                                                )}
                                                {(service.startLocation || service.fromLocation) && (
                                                    <p>
                                                        <strong>Start location: </strong>
                                                        {service.startLocation || service.fromLocation}
                                                    </p>
                                                )}
                                                {(service.endLocation || service.toLocation) && (
                                                    <p>
                                                        <strong>End location: </strong>
                                                        {service.endLocation || service.toLocation}
                                                    </p>
                                                )}
                                            </>
                                        )}
                                    </div>
                                </Col>

                                <Col md={4}>
                                    <Card className="border-0 bg-light">
                                        <Card.Body>
                                            <p className="text-muted mb-1">Giá dịch vụ</p>
                                            <h3 className="text-danger fw-bold mb-4">
                                                {price.toLocaleString('vi-VN')} VNĐ
                                            </h3>

                                            <Button
                                                size="lg"
                                                className="w-100"
                                                onClick={() => navigate(`/booking/${type}/${id}`)}
                                            >
                                                Đặt Dịch Vụ Ngay
                                            </Button>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>
                </Tab>

                <Tab eventKey="reviews" title="Đánh giá">
                    <Card className="border-0 shadow-sm mb-5">
                        <Card.Body className="p-4">
                            <h4 className="fw-bold mb-3">Đánh giá nhà cung cấp</h4>

                            {reviews.length > 0 ? (
                                reviews.map((review) => (
                                    <Card className="mb-3" key={review.id}>
                                        <Card.Body>
                                            <p className="mb-1">
                                                <strong>Rating: </strong>
                                                {review.rating}/5
                                            </p>
                                            <p className="mb-0">{review.comment}</p>
                                        </Card.Body>
                                    </Card>
                                ))
                            ) : (
                                <p className="text-muted">Chưa có đánh giá nào.</p>
                            )}

                            <Form onSubmit={submitReview} className="mt-4">
                                <Form.Group className="mb-3">
                                    <Form.Label>Rating</Form.Label>
                                    <Form.Select
                                        value={rating}
                                        onChange={(e) => setRating(e.target.value)}
                                    >
                                        <option value="1">1 sao</option>
                                        <option value="2">2 sao</option>
                                        <option value="3">3 sao</option>
                                        <option value="4">4 sao</option>
                                        <option value="5">5 sao</option>
                                    </Form.Select>
                                </Form.Group>

                                <Form.Group className="mb-3">
                                    <Form.Label>Comment</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        value={comment}
                                        onChange={(e) => setComment(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Button type="submit">
                                    Gửi đánh giá
                                </Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Tab>
            </Tabs>
        </Container>
    );
};

export default ServiceDetail;