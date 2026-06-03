import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import SearchForm from "./SearchForm";
import cookies from 'react-cookies';

const ServiceCard = ({ item, typeParam }) => {
    const navigate = useNavigate();
    
    // Đã map đúng biến Backend: Tours(title), Flights(flightCode), Hotels(hotelName)
    const name = item.title || item.flightCode || item.hotelName || 'Chuyến xe khách';
    const price = item.price || (item.hotelRoomsSet && item.hotelRoomsSet.length > 0 ? item.hotelRoomsSet[0].pricePerNight : 0);
    const image = item.thumbnail || 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e';

    return (
        <Card className="shadow-sm border-0 mb-4 h-100" style={{ borderRadius: '15px', overflow: 'hidden' }}>
            <Card.Img variant="top" src={image} style={{ height: '200px', objectFit: 'cover' }} />
            <Card.Body className="d-flex flex-column">
                <Card.Title className="fw-bold fs-6 text-truncate text-dark">{name}</Card.Title>
                <Card.Text className="text-danger fw-bold fs-5 mt-auto">
                    {price > 0 ? `${Number(price).toLocaleString('vi-VN')} VNĐ` : "Liên hệ"}
                </Card.Text>
                <Button 
                    variant="primary" 
                    className="w-100 mt-2 fw-bold" 
                    style={{ borderRadius: '8px' }}
                    onClick={() => navigate(`/services/${typeParam}/${item.id}`)}
                >
                    Xem chi tiết
                </Button>
            </Card.Body>
        </Card>
    );
};

const Home = () => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(false);
    const [search, setSearch] = useState({ location: "", type: "", departureTime: "", price: "" });
    const [currentTypeParam, setCurrentTypeParam] = useState("tours");
    const [isSearched, setIsSearched] = useState(false);
    const navigate = useNavigate();

    const getEndpointKey = (type) => {
        switch (type) {
            case "Khách sạn": return "hotels";
            case "Tour": return "tours";
            case "Vé máy bay": return "flights";
            case "Xe khách": return "busTrips";
            default: return "tours"; 
        }
    };

    const getTypeParam = (type) => {
        switch (type) {
            case "Khách sạn": return "hotels";
            case "Tour": return "tours";
            case "Vé máy bay": return "flights";
            case "Xe khách": return "bus-trips";
            default: return "tours";
        }
    };

    useEffect(() => {
        const loadInitialData = async () => {
            try {
                setLoading(true);
                let res = await Apis.get(endpoints["tours"]);
                setServices(Array.isArray(res.data) ? res.data : []);
                setCurrentTypeParam("tours");
                setIsSearched(false);
            } catch (err) {
                console.error(err);
                setServices([]);
            } finally {
                setLoading(false);
            }
        };
        loadInitialData();
    }, []);

    const handleSearch = async () => {
        // 🔒 KIỂM TRA BẢO MẬT: Bắt buộc đăng nhập mới cho tìm kiếm
        const token = cookies.load('token');
        if (!token) {
            alert("Vui lòng đăng nhập tài khoản để sử dụng chức năng tìm kiếm!");
            navigate('/user');
            return;
        }

        try {
            setLoading(true);
            const key = getEndpointKey(search.type);
            const param = getTypeParam(search.type);
            setCurrentTypeParam(param);
            setIsSearched(true);

            const queryParams = {};
            if (search.location) queryParams.location = search.location;
            if (search.departureTime) queryParams.departureTime = search.departureTime;
            if (search.price) queryParams.price = search.price;

            let res = await Apis.get(endpoints[key], { params: queryParams });
            setServices(Array.isArray(res.data) ? res.data : []);
        } catch (err) {
            console.error(err);
            setServices([]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <div
                style={{
                    height: "400px",
                    backgroundImage: "url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e')",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    position: "relative",
                }}
            >
                <div
                    style={{
                        position: "absolute",
                        inset: 0,
                        background: "rgba(0,0,0,0.45)",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        flexDirection: "column",
                    }}
                >
                    <h1 className="text-white fw-bold text-center" style={{ fontSize: "3rem" }}>
                        Khám phá thế giới cùng chúng tôi
                    </h1>

                    <p className="text-white fs-5">
                        Đặt vé nhanh chóng - Trải nghiệm tuyệt vời
                    </p>
                </div>
            </div>

            <Container className="mb-5">
                <SearchForm
                    search={search}
                    setSearch={setSearch}
                    handleSearch={handleSearch}
                />

                <div className="mt-5">
                    <h2 className="fw-bold mb-4">
                        {isSearched ? "🔍 Kết quả tìm kiếm phù hợp" : "🔥 Dịch vụ nổi bật"}
                    </h2>

                    {loading ? (
                        <MySpinner />
                    ) : (
                        <Row>
                            {Array.isArray(services) && services.length > 0 ? (
                                services.map((item) => (
                                    <Col md={3} sm={6} key={item.id} className="mb-4">
                                        <ServiceCard
                                            item={item}
                                            typeParam={currentTypeParam}
                                        />
                                    </Col>
                                ))
                            ) : (
                                <Col xs={12}>
                                    <p
                                        className="text-center text-muted py-5 fs-5 bg-light rounded"
                                        style={{ border: "1px dashed #ccc" }}
                                    >
                                        Không tìm thấy dịch vụ nào phù hợp.
                                    </p>
                                </Col>
                            )}
                        </Row>
                    )}
                </div>
            </Container>
        </>
    );
};

export default Home;