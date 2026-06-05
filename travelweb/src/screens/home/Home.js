import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";
import SearchForm from "./SearchForm";

const ServiceCard = ({ item, typeParam }) => {
    const navigate = useNavigate();


    const name = item.title || item.flightCode || item.hotelName || 'Chuyến xe khách';
    const image = item.thumbnail || item.image || 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e';


    let priceText = "Liên hệ giá";

    if (typeParam === "HOTEL") {

        if (item.hotelRoomsSet && item.hotelRoomsSet.length > 0) {
            const prices = item.hotelRoomsSet.map(r => r.pricePerNight).filter(p => p > 0);
            if (prices.length > 0) {
                const minPrice = Math.min(...prices);
                priceText = `Giá từ: ${Number(minPrice).toLocaleString('vi-VN')} Đ / đêm`;
            } else {
                priceText = "Xem giá phòng";
            }
        } else if (item.price > 0) {
            priceText = `Giá từ: ${Number(item.price).toLocaleString('vi-VN')} Đ / đêm`;
        } else {
            priceText = "Xem giá phòng";
        }
    } else {

        const flatPrice = item.price || item.ticketPrice || 0;
        priceText = flatPrice > 0 ? `${Number(flatPrice).toLocaleString('vi-VN')} VNĐ` : "Liên hệ giá";
    }

    const idParam = item.id;

    return (
        <Card className="shadow-sm border-0 mb-4 h-100" style={{ borderRadius: '15px', overflow: 'hidden' }}>
            <Card.Img variant="top" src={image} style={{ height: '200px', objectFit: 'cover' }} />
            <Card.Body className="d-flex flex-column justify-content-between">
                <div>
                    <Card.Title className="fw-bold text-dark text-truncate" style={{ fontSize: '1.15rem' }}>
                        {name}
                    </Card.Title>
                    <Card.Text className={typeParam === "HOTEL" ? "text-primary fw-bold mb-3" : "text-danger fw-bold mb-3"} style={{ fontSize: '1rem' }}>
                        {priceText}
                    </Card.Text>
                </div>
                <Button
                    variant="outline-primary"
                    className="w-100 border-2 fw-bold"
                    style={{ borderRadius: '8px' }}
                    onClick={() => navigate(`/services/${typeParam}/${idParam}`)}
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
    const [isSearched, setIsSearched] = useState(false);
    const [currentType, setCurrentType] = useState("TOUR");
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 20;

    useEffect(() => {
        const loadHotServices = async () => {
            try {
                setLoading(true);
                const res = await Apis.get(endpoints['tours']);
                setServices(res.data);
                setCurrentType("TOUR");
            } catch (err) {
                console.error("Lỗi tải dịch vụ nổi bật:", err);
            } finally {
                setLoading(false);
            }
        };
        loadHotServices();
    }, []);


    const handleSearchData = (data, searchType) => {
        setServices(data);
        setIsSearched(true);


        if (searchType === "Khách sạn") setCurrentType("HOTEL");
        else if (searchType === "Vé máy bay") setCurrentType("FLIGHT");
        else if (searchType === "Xe khách") setCurrentType("BUS");
        else setCurrentType("TOUR");
    };

    const safeServices = Array.isArray(services) ? services : [];
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = safeServices.slice(indexOfFirstItem, indexOfLastItem);
    const totalPages = Math.ceil(safeServices.length / itemsPerPage);







    return (
        <div>
            <div
                className="position-relative text-white text-center d-flex align-items-center justify-content-center"
                style={{
                    backgroundImage: `linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('https://images.unsplash.com/photo-1469854523086-cc02fe5d8800')`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    height: '60vh',
                }}
            >
                <div>
                    <h1 className="fw-bold display-4 mb-2">Khám Phá Hành Trình Của Bạn</h1>
                    <p className="fs-5 opacity-75">Tìm kiếm tour du lịch, khách sạn và vé phương tiện tốt nhất</p>
                </div>
            </div>

            <Container style={{ marginTop: '-50px', position: 'relative', zIndex: 10 }}>
                <Row className="justify-content-center">
                    <Col md={10}>

                        <SearchForm onSearchSuccess={handleSearchData} />
                    </Col>
                </Row>
            </Container>

            <Container className="mt-5">
                <h2 className="fw-bold mb-4">
                    {isSearched ? "  Kết quả tìm kiếm phù hợp" : "  Dịch vụ nổi bật"}
                </h2>
                {loading ? (
                    <MySpinner />
                ) : (
                    <>
                        <Row>
                            {currentItems.length > 0 ? (
                                currentItems.map((item, index) => (
                                    <Col key={item.id || index} sm={6} md={4} lg={3} className="mb-4">
                                        <ServiceCard item={item} typeParam={currentType} />
                                    </Col>
                                ))
                            ) : (
                                <Col className="text-center py-5">
                                    <h5 className="text-muted">Không tìm thấy dịch vụ nào phù hợp với yêu cầu của bạn.</h5>
                                </Col>
                            )}
                        </Row>


                        {totalPages > 1 && (
                            <div className="d-flex justify-content-center mt-4">
                                <ul className="pagination">
                                    <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                                        <button className="page-link" onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}>
                                            Trước
                                        </button>
                                    </li>

                                    {[...Array(totalPages)].map((_, index) => (
                                        <li key={index} className={`page-item ${currentPage === index + 1 ? 'active' : ''}`}>
                                            <button className="page-link" onClick={() => setCurrentPage(index + 1)}>
                                                {index + 1}
                                            </button>
                                        </li>
                                    ))}

                                    <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
                                        <button className="page-link" onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}>
                                            Sau
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        )}
                    </>
                )}
            </Container>
        </div>
    );
};

export default Home;