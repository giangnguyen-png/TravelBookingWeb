import { useState } from "react";
import { Card, Row, Col, Form, Button } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";

const SearchForm = ({ onSearchSuccess }) => {
    const [search, setSearch] = useState({
        location: "",
        type: "",
        departureTime: "",
        price: ""
    });
    const [loading, setLoading] = useState(false);

    const change = (e, field) => {
        setSearch({
            ...search,
            [field]: e.target.value
        });
    };

    const handleSearchClick = async () => {
        if (!search.type) {
            alert("Vui lòng chọn Loại hình dịch vụ cần tìm kiếm!");
            return;
        }

        try {
            setLoading(true);
            
            // Khớp nối chuẩn xác 100% các Key từ file Apis.js bạn vừa gửi
            let endpoint = endpoints['tours'];
            if (search.type === "Khách sạn") endpoint = endpoints['hotels'];
            else if (search.type === "Vé máy bay") endpoint = endpoints['flights'];
            else if (search.type === "Xe khách") endpoint = endpoints['busTrips']; // Đã sửa từ 'bus-trips' thành 'busTrips'

            const queryParams = new URLSearchParams();
            if (search.location) queryParams.append("location", search.location);
            if (search.price) queryParams.append("price", search.price);
            if (search.departureTime) queryParams.append("departureTime", search.departureTime);

            const res = await Apis.get(`${endpoint}?${queryParams.toString()}`);
            
            if (onSearchSuccess) {
                onSearchSuccess(res.data, search.type);
            }
        } catch (err) {
            console.error("Lỗi tìm kiếm:", err);
            alert("Không tìm thấy kết quả phù hợp!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Card
            className="shadow-lg border-0"
            style={{
                marginTop: "-70px",
                borderRadius: "20px",
                position: "relative",
                zIndex: 10
            }}
        >
            <Card.Body className="p-4">
                <h4 className="fw-bold mb-4 text-center">
                    🔍 Tìm kiếm dịch vụ du lịch
                </h4>
                <Row className="g-3">
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label className="fw-bold">Địa điểm</Form.Label>
                            <Form.Select
                                value={search.location}
                                onChange={(e) => change(e, "location")}
                            >
                                <option value="">Chọn địa điểm...</option>
                                <option value="Đà Lạt">Đà Lạt</option>
                                <option value="Hà Nội">Hà Nội</option>
                                <option value="TP.HCM">TP.HCM</option>
                                <option value="Đà Nẵng">Đà Nẵng</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label className="fw-bold">Loại hình</Form.Label>
                            <Form.Select
                                value={search.type}
                                onChange={(e) => change(e, "type")}
                            >
                                <option value="">Chọn loại hình...</option>
                                <option value="Khách sạn">Khách sạn</option>
                                <option value="Tour">Tour</option>
                                <option value="Vé máy bay">Vé máy bay</option>
                                <option value="Xe khách">Xe khách</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label className="fw-bold">Thời gian</Form.Label>
                            <Form.Control
                                type="date"
                                value={search.departureTime}
                                onChange={(e) => change(e, "departureTime")}
                            />
                        </Form.Group>
                    </Col>
                    <Col md={3}>
                        <Form.Group>
                            <Form.Label className="fw-bold">Mức giá tối đa</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Nhập giá..."
                                value={search.price}
                                onChange={(e) => change(e, "price")}
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <div className="text-center mt-4">
                    <Button
                        variant="primary"
                        size="lg"
                        className="px-5 fw-bold"
                        onClick={handleSearchClick}
                        disabled={loading}
                    >
                        {loading ? "Đang tìm..." : "🔍 Tìm kiếm ngay"}
                    </Button>
                </div>
            </Card.Body>
        </Card>
    );
};

export default SearchForm;