import { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row, Table } from "react-bootstrap";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../../components/MySpinner";

const compareTypes = [
    { value: "HOTEL", endpoint: "hotels", label: "Khách sạn" },
    { value: "TOUR", endpoint: "tours", label: "Tour" },
    { value: "FLIGHT", endpoint: "flights", label: "Vé máy bay" },
    { value: "BUS", endpoint: "busTrips", label: "Xe khách" }
];

const getName = (item, type) => {
    if (type === "HOTEL") return item.hotelName;
    if (type === "TOUR") return item.title;
    if (type === "FLIGHT") return item.flightCode;
    return `Chuyến xe #${item.id}`;
};

const formatMoney = (value) => Number(value || 0).toLocaleString("vi-VN");

const renderHotelRooms = (rooms = []) => {
    if (!rooms.length) {
        return <span className="text-muted">Chưa có phòng</span>;
    }

    return (
        <div className="d-flex flex-column gap-2">
            {rooms.map((room) => (
                <div key={room.id} className="p-2 bg-light rounded border">
                    <div className="fw-bold">{room.roomName}</div>
                    <div>Loại phòng: {room.roomType}</div>
                    <div>
                        Giá mỗi đêm: <span className="text-danger fw-bold">{formatMoney(room.pricePerNight)} VNĐ</span>
                    </div>
                    <div>Số phòng trống: {room.availableRooms}</div>
                    {room.description && <div className="text-muted">{room.description}</div>}
                </div>
            ))}
        </div>
    );
};

const CompareServices = () => {
    const [type, setType] = useState("HOTEL");
    const [services, setServices] = useState([]);
    const [firstId, setFirstId] = useState("");
    const [secondId, setSecondId] = useState("");
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const typeConfig = compareTypes.find((item) => item.value === type) || compareTypes[0];
    const isHotel = type === "HOTEL";

    useEffect(() => {
        const loadServices = async () => {
            setError("");
            setItems([]);
            setFirstId("");
            setSecondId("");

            try {
                setLoading(true);
                const res = await Apis.get(endpoints[typeConfig.endpoint]);
                const list = Array.isArray(res.data) ? res.data : [];
                setServices(list);
                if (list.length < 2) {
                    setError("Không đủ dịch vụ để so sánh");
                }
            } catch (err) {
                console.error(err);
                setServices([]);
                setError("Không đủ dịch vụ để so sánh");
            } finally {
                setLoading(false);
            }
        };

        loadServices();
    }, [type, typeConfig.endpoint]);

    const compare = async (e) => {
        e.preventDefault();
        setError("");
        setItems([]);

        if (!firstId || !secondId || firstId === secondId) {
            setError("Vui lòng chọn 2 dịch vụ khác nhau.");
            return;
        }

        try {
            setLoading(true);
            const res = await Apis.get(endpoints.compare, {
                params: {
                    type,
                    ids: `${firstId},${secondId}`
                }
            });

            const list = Array.isArray(res.data) ? res.data : [];
            if (list.length < 2) {
                setError("Không đủ dịch vụ để so sánh");
                return;
            }
            setItems(list);
        } catch (err) {
            console.error(err);
            setError("Không đủ dịch vụ để so sánh");
        } finally {
            setLoading(false);
        }
    };

    const renderRows = () => {
        if (isHotel) {
            return (
                <>
                    <tr>
                        <th>Địa chỉ</th>
                        {items.map((item) => <td key={`${item.id}-address`}>{item.address}</td>)}
                    </tr>
                    <tr>
                        <th>Địa điểm</th>
                        {items.map((item) => <td key={`${item.id}-location`}>{item.location}</td>)}
                    </tr>
                    <tr>
                        <th>Mô tả</th>
                        {items.map((item) => <td key={`${item.id}-description`}>{item.description}</td>)}
                    </tr>
                    <tr>
                        <th>Phòng</th>
                        {items.map((item) => <td key={`${item.id}-rooms`}>{renderHotelRooms(item.rooms)}</td>)}
                    </tr>
                </>
            );
        }

        return (
            <>
                <tr>
                    <th>Giá</th>
                    {items.map((item) => <td key={`${item.id}-price`} className="text-danger fw-bold">{formatMoney(item.price)} VNĐ</td>)}
                </tr>
                <tr>
                    <th>Thời gian</th>
                    {items.map((item) => <td key={`${item.id}-time`}>{item.time}</td>)}
                </tr>
                <tr>
                    <th>{type === "TOUR" ? "Số chỗ còn trống" : "Số ghế còn trống"}</th>
                    {items.map((item) => <td key={`${item.id}-quantity`}>{item.quantity}</td>)}
                </tr>
                <tr>
                    <th>Điểm khởi hành</th>
                    {items.map((item) => <td key={`${item.id}-departure`}>{item.departureLocation}</td>)}
                </tr>
                <tr>
                    <th>Điểm đến</th>
                    {items.map((item) => <td key={`${item.id}-destination`}>{item.destinationLocation}</td>)}
                </tr>
                {type === "TOUR" && (
                    <tr>
                        <th>Số ngày</th>
                        {items.map((item) => <td key={`${item.id}-duration`}>{item.durationDays}</td>)}
                    </tr>
                )}
            </>
        );
    };

    return (
        <Container className="my-5">
            <Row className="justify-content-center">
                <Col lg={10}>
                    <h2 className="fw-bold mb-4 text-primary">So sánh dịch vụ</h2>

                    <Card className="border-0 shadow-sm mb-4">
                        <Card.Body>
                            <Form onSubmit={compare}>
                                <Row className="align-items-end">
                                    <Col md={4} className="mb-3">
                                        <Form.Label className="fw-bold">Loại dịch vụ</Form.Label>
                                        <Form.Select value={type} onChange={(e) => setType(e.target.value)}>
                                            {compareTypes.map((item) => (
                                                <option key={item.value} value={item.value}>{item.label}</option>
                                            ))}
                                        </Form.Select>
                                    </Col>
                                    <Col md={3} className="mb-3">
                                        <Form.Label className="fw-bold">Dịch vụ thứ nhất</Form.Label>
                                        <Form.Select value={firstId} onChange={(e) => setFirstId(e.target.value)} disabled={services.length < 2} required>
                                            <option value="">Chọn dịch vụ</option>
                                            {services.map((item) => (
                                                <option key={item.id} value={item.id}>{getName(item, type)}</option>
                                            ))}
                                        </Form.Select>
                                    </Col>
                                    <Col md={3} className="mb-3">
                                        <Form.Label className="fw-bold">Dịch vụ thứ hai</Form.Label>
                                        <Form.Select value={secondId} onChange={(e) => setSecondId(e.target.value)} disabled={services.length < 2} required>
                                            <option value="">Chọn dịch vụ</option>
                                            {services.map((item) => (
                                                <option key={item.id} value={item.id} disabled={String(item.id) === firstId}>
                                                    {getName(item, type)}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    </Col>
                                    <Col md={2} className="mb-3">
                                        <Button type="submit" className="w-100" disabled={loading || services.length < 2}>
                                            So sánh
                                        </Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>

                    {error && <Alert variant="danger">{error}</Alert>}
                    {loading && <MySpinner />}

                    {!loading && items.length === 2 && (
                        <Card className="border-0 shadow-sm">
                            <Table responsive hover className="m-0 align-middle">
                                <thead className="table-secondary">
                                    <tr>
                                        <th>Tiêu chí</th>
                                        {items.map((item) => <th key={item.id}>{item.name}</th>)}
                                    </tr>
                                </thead>
                                <tbody>{renderRows()}</tbody>
                            </Table>
                        </Card>
                    )}
                </Col>
            </Row>
        </Container>
    );
};

export default CompareServices;
