import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Button, Spinner, Badge, Form, Tab, Tabs, Table, Alert } from 'react-bootstrap';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import { useContext } from 'react';
import { MyUserContext } from '../../configs/MyContext';

const placeholderImage = 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e';


const getTitle = (service, type) => {
    if (type === 'FLIGHT') return `${service?.airlineName || 'Hãng hàng không'} - ${service?.flightCode || ''}`;
    if (type === 'BUS') return `${service?.brandName || 'Nhà xe'} (${service?.busType || 'Xe khách'})`;
    return (
        service?.title ||
        service?.name ||
        service?.hotelName ||
        'Dịch vụ du lịch'
    );
};


const getPrice = (service) => {
    return Number(
        service?.price ||
        service?.ticketPrice ||
        service?.pricePerNight ||
        0
    ).toLocaleString('vi-VN');
};

const getProviderId = (service) => {
    const candidates = [
        service?.providerId,
        service?.provider_id,
        service?.provider,
        service?.userId
    ];

    for (const item of candidates) {
        if (!item) continue;
        return typeof item === 'object' ? item.id : item;
    }

    return null;
};


const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) return "Chưa cập nhật";
    try {
        const date = new Date(dateTimeString);
        return date.toLocaleString('vi-VN', {
            hour: '2-digit',
            minute: '2-digit',
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    } catch (e) {
        return dateTimeString;
    }
};

const ServiceDetail = () => {
    const { type, id } = useParams();
    const navigate = useNavigate();
    const user = useContext(MyUserContext);

    const [service, setService] = useState(null);
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [reviews, setReviews] = useState([]);
    const [rating, setRating] = useState(5);
    const [comment, setComment] = useState('');

    useEffect(() => {
        const fetchServiceDetail = async () => {
            if (!id || id === 'undefined') {
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                let baseEndpoint = '';

                if (type === 'TOUR') baseEndpoint = endpoints['tours'];
                else if (type === 'HOTEL') baseEndpoint = endpoints['hotels'];
                else if (type === 'FLIGHT') baseEndpoint = endpoints['flights'];
                else if (type === 'BUS') baseEndpoint = endpoints['busTrips'];

                if (!baseEndpoint) {
                    console.error("Không tìm thấy endpoint cho loại dịch vụ:", type);
                    setLoading(false);
                    return;
                }

                let finalEndpoint = baseEndpoint;
                if (!finalEndpoint.endsWith('/')) {
                    finalEndpoint = `${finalEndpoint}/`;
                }
                finalEndpoint = `${finalEndpoint}${id}`;

                const res = await Apis.get(finalEndpoint);
                setService(res.data);

                if (type === 'HOTEL') {
                    try {
                        const roomEndpoint = endpoints['hotel-rooms'].replace(':hotelId', id);

                        const roomsRes = await Apis.get(roomEndpoint);
                        setRooms(roomsRes.data);
                    } catch (roomErr) {
                        console.error("Lỗi tải danh sách phòng khách sạn:", roomErr);
                    }
                }
            } catch (err) {
                console.error("Lỗi tải chi tiết dịch vụ:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchServiceDetail();
    }, [type, id]);


useEffect(() => {
    const loadReviews = async () => {

        const providerId = getProviderId(service);

        if (providerId && providerId !== 'undefined') {
            try {

                const res = await Apis.get(endpoints['provider-reviews'].replace(':providerId', providerId));


                if (res.data && res.data.items) {
                    setReviews(res.data.items);
                } else if (Array.isArray(res.data)) {
                    setReviews(res.data);
                }
            } catch (err) {
                console.error("Lỗi tải danh sách đánh giá ban đầu:", err);
            }
        }
    };

    if (service) {
        loadReviews();
    }
}, [service]);


const submitReview = async (e) => {
    e.preventDefault();
    const currentCustomerId = user?.id;
    if (!currentCustomerId) {
        alert("Bạn cần đăng nhập tài khoản trước khi gửi đánh giá!");
        return;
    }

    const currentProviderId = getProviderId(service);
    if (!currentProviderId) {
        alert("Không xác định được nhà cung cấp của dịch vụ này!");
        return;
    }

    try {
        const payload = {
            customerId: String(currentCustomerId),
            providerId: String(currentProviderId),
            rating: String(rating),
            comment: comment || ""
        };


        await authApis().post(endpoints['reviews'], payload);
        alert('Gửi đánh giá thành công!');
        setComment('');


        const res = await Apis.get(endpoints['provider-reviews'].replace(':providerId', currentProviderId));
        if (res.data && res.data.items) {
            setReviews(res.data.items);
        } else if (Array.isArray(res.data)) {
            setReviews(res.data);
        }
    } catch (err) {
        console.error("Lỗi khi gửi đánh giá:", err);
        alert("Gửi đánh giá thất bại!");
    }
};

const handleBooking = (roomItem = null) => {
    if (!user) {
        alert('Vui lòng đăng nhập để thực hiện đặt chỗ!');
        return;
    }


    const targetId = roomItem ? roomItem.id : id;


    navigate(`/booking/${type}/${targetId}`);
};

    if (loading) {
        return (
            <Container className="text-center my-5">
                <Spinner animation="border" variant="primary" />
                <p className="mt-2">Đang tải thông tin chi tiết...</p>
            </Container>
        );
    }

    if (!service || !id || id === 'undefined') {
        return (
            <Container className="my-5">
                <Alert variant="danger">
                    <h5>⚠️ Không tìm thấy dịch vụ yêu cầu!</h5>
                    <p>Vui lòng quay lại trang chủ và chọn chính xác vào dịch vụ.</p>
                </Alert>
            </Container>
        );
    }

    return (
        <Container className="my-4">
            <Row>
                <Col md={6}>
                    <Card className="shadow-sm border-0 mb-4">
                        <Card.Img
                            variant="top"
                            src={service.image || service.thumbnail || placeholderImage}
                            alt={getTitle(service, type)}
                            style={{ height: '400px', objectFit: 'cover', borderRadius: '12px' }}
                        />
                    </Card>
                </Col>
                <Col md={6}>
                    <div className="mb-3">
                        <Badge bg={type === 'HOTEL' ? 'info' : type === 'TOUR' ? 'success' : type === 'FLIGHT' ? 'primary' : 'warning'} className="mb-2 text-uppercase px-3 py-2">
                            {type === 'HOTEL' ? 'Khách sạn' : type === 'TOUR' ? 'Tour du lịch' : type === 'FLIGHT' ? 'Máy bay' : 'Xe khách'}
                        </Badge>
                        <h2 className="fw-bold text-dark">{getTitle(service, type)}</h2>

                        {type === 'HOTEL' && service.starRating && (
                            <div className="text-warning mb-2" style={{ fontSize: '1.2rem' }}>
                                {'★'.repeat(service.starRating)}{'☆'.repeat(5 - service.starRating)}
                                <span className="text-muted ms-2" style={{ fontSize: '1rem' }}>({service.starRating} sao)</span>
                            </div>
                        )}

                        {type === 'HOTEL' && service.address && (
                            <p className="text-muted mb-2">
                                <i className="bi bi-geo-alt-fill text-danger me-1"></i>
                                <b>Địa chỉ:</b> {service.address}
                            </p>
                        )}
                    </div>

                    <Card className="bg-light border-0 p-3 mb-4 rounded">
                        {type === 'HOTEL' ? (
                            <div>
                                <h5 className="text-primary fw-bold mb-2">Giới thiệu về khách sạn</h5>
                                <p style={{ whiteSpace: 'pre-line', color: '#495057', lineHeight: '1.6' }}>
                                    {service.description || 'Chưa có thông tin mô tả chi tiết cho khách sạn này.'}
                                </p>
                            </div>
                        ) : (
                            <div>
                            <h5 className="text-dark fw-bold mb-3 border-bottom pb-2">Thông Tin Lịch Trình Chi Tiết</h5>


                            {type === 'TOUR' && (
                                <div className="mb-3" style={{ fontSize: '1rem', lineHeight: '1.8' }}>
                                    <p className="mb-1">
                                        📍 <b>Điểm khởi hành:</b> {
                                            service.departureLocationId?.name ||
                                            service.departureLocation?.name ||
                                            (service.title && service.title.includes("-") ?
                                                (service.title.split("-")[0].toUpperCase().includes("HCM") ? "Hồ Chí Minh" : service.title.split("-")[0].replace("Tour", "").trim())
                                                : "Chưa cập nhật")
                                        }
                                    </p>
                                    <p className="mb-1">
                                        🏁 <b>Điểm đến:</b> {
                                            service.destinationLocationId?.name ||
                                            service.destinationLocation?.name ||
                                            (service.title && service.title.includes("-") ?
                                                (service.title.split("-")[1].trim().startsWith("Đà Lạt") ? "Đà Lạt" : service.title.split("-")[1].trim().split(" ")[0])
                                                : "Chưa cập nhật")
                                        }
                                    </p>
                                    <p className="mb-1">
                                        📅 <b>Thời gian khởi hành:</b> {formatDateTime(service.departureDate || service.departureTime)}
                                    </p>
                                    <p className="mb-1">
                                        ⏳ <b>Thời gian diễn ra:</b> {service.durationDays || service.duration || 0} ngày
                                    </p>
                                    <p className="mb-1">
                                        👥 <b>Số chỗ trống còn nhận:</b> <Badge bg="success">{service.availableSlots ?? service.availablePlaces ?? 0} chỗ</Badge>
                                    </p>
                                </div>
                            )}


                            {type === 'FLIGHT' && (
                                <div className="mb-3" style={{ fontSize: '1rem', lineHeight: '1.8' }}>
                                    <p className="mb-1">✈️ <b>Hãng bay:</b> {service.airlineName || 'Chưa cập nhật'}</p>
                                    <p className="mb-1">
                                        🛫 <b>Sân bay khởi hành:</b> {
                                            service.departureLocationId?.name ||
                                            service.departureLocation?.name ||

                                            (service.flightCode && service.flightCode.toUpperCase().startsWith("VN") ? "Sân bay Nội Bài (Hà Nội)" : "Hồ Chí Minh (SGN)")
                                        }
                                    </p>
                                    <p className="mb-1">
                                        🛬 <b>Sân bay hạ cánh:</b> {
                                            service.arrivalLocationId?.name ||
                                            service.arrivalLocation?.name ||
                                            (service.flightCode && service.flightCode.toUpperCase().includes("DL") ? "Sân bay Liên Khương (Đà Lạt)" : "Đà Nẵng (DAD)")
                                        }
                                    </p>
                                    <p className="mb-1">🕒 <b>Giờ cất cánh:</b> {formatDateTime(service.departureTime)}</p>
                                    <p className="mb-1">🛬 <b>Giờ hạ cánh dự kiến:</b> {formatDateTime(service.arrivalTime)}</p>
                                    <p className="mb-1">💺 <b>Số ghế trống hiện tại:</b> <Badge bg="primary">{service.availableSeats ?? 0} ghế</Badge></p>
                                </div>
                            )}


                            {type === 'BUS' && (
                                <div className="mb-3" style={{ fontSize: '1rem', lineHeight: '1.8' }}>
                                    <p className="mb-1">🚌 <b>Nhà xe quản lý:</b> {service.brandName || 'Chưa cập nhật'}</p>
                                    <p className="mb-1">🚐 <b>Dòng xe:</b> {service.busType || 'Chưa cập nhật'}</p>
                                    <p className="mb-1">
                                        📍 <b>Bến xe xuất phát:</b> {
                                            service.departureLocationId?.name ||
                                            service.departureLocation?.name ||
                                            (service.brandName && service.brandName.includes("Phương Trang") ? "Bến xe Miền Tây (TP.HCM)" : "Bến xe Giáp Bát (Hà Nội)")
                                        }
                                    </p>
                                    <p className="mb-1">
                                        🏁 <b>Bến xe điểm đến:</b> {
                                            service.arrivalLocationId?.name ||
                                            service.arrivalLocation?.name ||
                                            (service.brandName && service.brandName.includes("Đà Lạt") ? "Bến xe Liên tỉnh Đà Lạt" : "Bến xe Trung tâm Đà Nẵng")
                                        }
                                    </p>
                                    <p className="mb-1">🕒 <b>Giờ khởi hành:</b> {formatDateTime(service.departureTime)}</p>
                                    <p className="mb-1">🏁 <b>Giờ tới nơi dự kiến:</b> {formatDateTime(service.arrivalTime)}</p>
                                    <p className="mb-1">💺 <b>Số chỗ trống còn lại:</b> <Badge bg="warning" text="dark">{service.availableSeats ?? 0} giường</Badge></p>
                                </div>
                            )}

                            <div className="mt-3 pt-2 border-top">
                                <span className="text-muted" style={{ fontSize: '0.9rem' }}>Giá vé trọn gói:</span>
                                <h3 className="text-danger fw-bold">{getPrice(service)} VNĐ</h3>
                            </div>

                            <Button onClick={() => handleBooking(null)} variant="primary" className="mt-3 w-100 py-2 fw-bold" style={{ borderRadius: '8px' }}>
                                Tiến hành đặt chỗ ngay
                            </Button>
                        </div>
                        )}
                    </Card>
                </Col>
            </Row>


            {type === 'HOTEL' && (
                <Row className="mb-5">
                    <Col>
                        <Card className="shadow-sm border-0">
                            <Card.Body>
                                <h4 className="fw-bold text-primary mb-4 border-bottom pb-2">
                                    Danh mục loại phòng & Giá phòng chi tiết
                                </h4>
                                {rooms.length === 0 ? (
                                    <Alert variant="warning">Khách sạn này hiện tại chưa cập nhật danh mục phòng hoặc đã hết phòng.</Alert>
                                ) : (
                                    <Table responsive hover className="align-middle">
                                        <thead className="table-dark">
                                            <tr>
                                                <th>Hình ảnh</th>
                                                <th>Tên phòng</th>
                                                <th>Loại phòng</th>
                                                <th>Mô tả chi tiết</th>
                                                <th>Giá mỗi đêm</th>
                                                <th>Số phòng trống</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {rooms.map((room) => (
                                                <tr key={room.id}>
                                                    <td>
                                                        <img
                                                            src={room.image || placeholderImage}
                                                            alt={room.roomName}
                                                            style={{ width: '100px', height: '70px', objectFit: 'cover', borderRadius: '6px' }}
                                                        />
                                                    </td>
                                                    <td><b className="text-dark">{room.roomName}</b></td>
                                                    <td>
                                                        <Badge bg={room.roomType === 'VIP' ? 'danger' : 'secondary'}>
                                                            {room.roomType}
                                                        </Badge>
                                                    </td>
                                                    <td style={{ maxWidth: '250px', fontSize: '0.9rem', color: '#6c757d' }}>
                                                        {room.description}
                                                    </td>
                                                    <td>
                                                        <b className="text-danger">
                                                            {Number(room.pricePerNight).toLocaleString('vi-VN')} đ
                                                        </b>
                                                    </td>
                                                    <td className="text-center">{room.availableRooms}</td>
                                                    <td>
                                                        <Button
                                                            variant="success"
                                                            size="sm"
                                                            className="px-3 fw-bold"
                                                            disabled={room.availableRooms <= 0}
                                                            onClick={() => handleBooking(room)}
                                                        >
                                                            {room.availableRooms > 0 ? 'Đặt phòng' : 'Hết phòng'}
                                                        </Button>
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </Table>
                                )}
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            )}

            <Tabs defaultActiveKey="reviews" className="mb-3">
                <Tab eventKey="reviews" title={`Nhận xét & Đánh giá (${reviews.length})`}>
                    <Row className="mt-3">
                        <Col md={6}>
                            <h5 className="fw-bold mb-3">Các bình luận trước đó</h5>
                            {reviews.length === 0 ? (
                                <p className="text-muted style-italic">Chưa có lượt đánh giá nào cho nhà cung cấp này.</p>
                            ) : (
                                reviews.map((rev) => (
                                    <Card key={rev.id} className="mb-3 border-0 bg-light p-3 rounded">
                                        <div className="d-flex justify-content-between align-items-center mb-1">
                                            <b className="text-primary">{rev.customerId?.fullName || rev.customerId?.username || 'Khách ẩn danh'}</b>
                                            <span className="text-warning">{'★'.repeat(rev.rating)}</span>
                                        </div>
                                        <p className="mb-1 text-dark" style={{ fontSize: '0.95rem' }}>{rev.comment}</p>
                                        <small className="text-muted">
                                            {rev.createdAt ? new Date(rev.createdAt).toLocaleDateString('vi-VN') : ''}
                                        </small>
                                    </Card>
                                ))
                            )}
                        </Col>
                        <Col md={6}>
                            <Card className="p-4 shadow-sm border-0 bg-white rounded">
                                <h5 className="fw-bold mb-3 text-dark">Để lại nhận xét của bạn</h5>
                                <Form onSubmit={submitReview}>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Mức độ hài lòng (Số sao)</Form.Label>
                                        <Form.Select value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                                            <option value={5}>5 Sao - Tuyệt vời</option>
                                            <option value={4}>4 Sao - Tốt</option>
                                            <option value={3}>3 Sao - Bình thường</option>
                                            <option value={2}>2 Sao - Tệ</option>
                                            <option value={1}>1 Sao - Quá tệ</option>
                                        </Form.Select>
                                    </Form.Group>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Nội dung nhận xét</Form.Label>
                                        <Form.Control
                                            as="textarea"
                                            rows={3}
                                            value={comment}
                                            onChange={(e) => setComment(e.target.value)}
                                            placeholder="Nhập trải nghiệm thực tế của bạn tại đây..."
                                            required
                                        />
                                    </Form.Group>
                                    <Button type="submit" variant="dark" className="w-100 py-2">
                                        Gửi phản hồi
                                    </Button>
                                </Form>
                            </Card>
                        </Col>
                    </Row>
                </Tab>
            </Tabs>
        </Container>
    );
};

export default ServiceDetail;
