import { Col, Card, Button } from "react-bootstrap";

const ServiceCard = ({ s }) => {
    return (
        <Col md={4} lg={3} className="mb-4">
            <Card className="h-100">
                <Card.Img
                    variant="top"
                    src={s.image}
                    style={{ height: "200px", objectFit: "cover" }}
                />
                <Card.Body>
                    <Card.Title>{s.name}</Card.Title>
                    <Card.Text><strong>Loại:</strong> {s.type}</Card.Text>
                    <Card.Text><strong>Địa điểm:</strong> {s.location}</Card.Text>
                    <Card.Text><strong>Giá:</strong> {s.price}</Card.Text>
                    <Button variant="success">Xem chi tiết</Button>
                </Card.Body>
            </Card>
        </Col>
    );
};

export default ServiceCard;