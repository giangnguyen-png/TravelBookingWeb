import React from 'react';
import { Button, Card, Col, Form, Modal, Row, Table } from 'react-bootstrap';

const ProviderServices = ({
    labels,
    businessType,
    isHotel,
    services,
    locations,
    showServiceModal,
    editingService,
    serviceForm,
    roomForm,
    showRoomModal,
    openAddService,
    openEditService,
    openRoomModal,
    deleteService,
    updateService,
    updateRoom,
    setThumbnailFile,
    setRoomImageFile,
    saveService,
    saveRoom,
    closeServiceModal,
    closeRoomModal
}) => {
    const serviceName = (service) => {
        if (businessType === 'HOTEL') return service.hotelName;
        if (businessType === 'TOUR_COMPANY') return service.title;
        if (businessType === 'AIRLINE') return service.flightCode;
        return `Chuyến xe #${service.id}`;
    };

    const locationOptions = (
        <>
            <option value="">Chọn ��9a �iỒm</option>
            {locations.map((location) => (
                <option key={location.id} value={location.id}>
                    {[location.province, location.country].filter(Boolean).join(', ')}
                </option>
            ))}
        </>
    );

    const imageInput = (required) => (
        <Form.Group className="mb-3">
            <Form.Label>Ảnh �ại di�!n</Form.Label>
            <Form.Control required={required} type="file" accept="image/*" onChange={(e) => setThumbnailFile(e.target.files[0] || null)} />
        </Form.Group>
    );

    const roomFields = (
        <Card className="border-0 bg-light mt-4">
            <Card.Body>
                <h5 className="fw-bold mb-3">Thông tin phòng khách sạn</h5>
                <Form.Group className="mb-3">
                    <Form.Label>Tên phòng</Form.Label>
                    <Form.Control required value={roomForm.roomName} onChange={(e) => updateRoom('roomName', e.target.value)} />
                </Form.Group>
                <Row>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Loại phòng</Form.Label>
                            <Form.Select required value={roomForm.roomType} onChange={(e) => updateRoom('roomType', e.target.value)}>
                                <option value="STANDARD">STANDARD</option>
                                <option value="DELUXE">DELUXE</option>
                                <option value="VIP">VIP</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>
                    <Col md={6}>
                        <Form.Group className="mb-3">
                            <Form.Label>Giá m�i �êm</Form.Label>
                            <Form.Control required min="1" type="number" value={roomForm.pricePerNight} onChange={(e) => updateRoom('pricePerNight', e.target.value)} />
                        </Form.Group>
                    </Col>
                </Row>
                <Form.Group className="mb-3">
                    <Form.Label>S� phòng còn tr�ng</Form.Label>
                    <Form.Control required min="0" type="number" value={roomForm.availableRooms} onChange={(e) => updateRoom('availableRooms', e.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Mô tả phòng</Form.Label>
                    <Form.Control required as="textarea" rows={3} value={roomForm.description} onChange={(e) => updateRoom('description', e.target.value)} />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Ảnh phòng</Form.Label>
                    <Form.Control required type="file" accept="image/*" onChange={(e) => setRoomImageFile(e.target.files[0] || null)} />
                </Form.Group>
            </Card.Body>
        </Card>
    );

    const serviceFields = () => {
        if (businessType === 'HOTEL') {
            return (
                <>
                    <Form.Group className="mb-3">
                        <Form.Label>Tên khách sạn</Form.Label>
                        <Form.Control required value={serviceForm.hotelName} onChange={(e) => updateService('hotelName', e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Mô tả khách sạn</Form.Label>
                        <Form.Control required as="textarea" rows={3} value={serviceForm.description} onChange={(e) => updateService('description', e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Đ�9a ch�0</Form.Label>
                        <Form.Control required value={serviceForm.address} onChange={(e) => updateService('address', e.target.value)} />
                    </Form.Group>
                    {imageInput(!editingService)}
                    <Form.Group className="mb-3">
                        <Form.Label>Đ�9a �iỒm</Form.Label>
                        <Form.Select required value={serviceForm.locationId} onChange={(e) => updateService('locationId', e.target.value)}>
                            {locationOptions}
                        </Form.Select>
                    </Form.Group>
                    {!editingService && roomFields}
                </>
            );
        }

        if (businessType === 'TOUR_COMPANY') {
            return (
                <>
                    <Form.Group className="mb-3">
                        <Form.Label>Tên tour</Form.Label>
                        <Form.Control required value={serviceForm.title} onChange={(e) => updateService('title', e.target.value)} />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Mô tả</Form.Label>
                        <Form.Control required as="textarea" rows={3} value={serviceForm.description} onChange={(e) => updateService('description', e.target.value)} />
                    </Form.Group>
                    <Row>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Ngày kh�xi hành</Form.Label><Form.Control required type="datetime-local" value={serviceForm.departureDate} onChange={(e) => updateService('departureDate', e.target.value)} /></Form.Group></Col>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>S� ngày</Form.Label><Form.Control required min="1" type="number" value={serviceForm.durationDays} onChange={(e) => updateService('durationDays', e.target.value)} /></Form.Group></Col>
                    </Row>
                    <Row>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>Giá tour</Form.Label><Form.Control required min="1" type="number" value={serviceForm.price} onChange={(e) => updateService('price', e.target.value)} /></Form.Group></Col>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>S� ch� còn tr�ng</Form.Label><Form.Control required min="0" type="number" value={serviceForm.availableSlots} onChange={(e) => updateService('availableSlots', e.target.value)} /></Form.Group></Col>
                    </Row>
                    {imageInput(!editingService)}
                    <Row>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>ĐiỒm kh�xi hành</Form.Label><Form.Select required value={serviceForm.departureLocationId} onChange={(e) => updateService('departureLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                        <Col md={6}><Form.Group className="mb-3"><Form.Label>ĐiỒm �ến</Form.Label><Form.Select required value={serviceForm.destinationLocationId} onChange={(e) => updateService('destinationLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                    </Row>
                </>
            );
        }

        return (
            <>
                {businessType === 'AIRLINE' && (
                    <>
                        <Form.Group className="mb-3"><Form.Label>Mã chuyến bay</Form.Label><Form.Control required value={serviceForm.flightCode} onChange={(e) => updateService('flightCode', e.target.value)} /></Form.Group>
                        {imageInput(!editingService)}
                    </>
                )}
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Thời gian kh�xi hành</Form.Label><Form.Control required type="datetime-local" value={serviceForm.departureTime} onChange={(e) => updateService('departureTime', e.target.value)} /></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Thời gian �ến</Form.Label><Form.Control required type="datetime-local" value={serviceForm.arrivalTime} onChange={(e) => updateService('arrivalTime', e.target.value)} /></Form.Group></Col>
                </Row>
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>Giá vé</Form.Label><Form.Control required min="1" type="number" value={serviceForm.price} onChange={(e) => updateService('price', e.target.value)} /></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>S� ghế còn tr�ng</Form.Label><Form.Control required min="0" type="number" value={serviceForm.availableSeats} onChange={(e) => updateService('availableSeats', e.target.value)} /></Form.Group></Col>
                </Row>
                <Row>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>ĐiỒm kh�xi hành</Form.Label><Form.Select required value={serviceForm.departureLocationId} onChange={(e) => updateService('departureLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                    <Col md={6}><Form.Group className="mb-3"><Form.Label>ĐiỒm �ến</Form.Label><Form.Select required value={serviceForm.arrivalLocationId} onChange={(e) => updateService('arrivalLocationId', e.target.value)}>{locationOptions}</Form.Select></Form.Group></Col>
                </Row>
            </>
        );
    };

    return (
        <>
            <div>
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h4 className="fw-bold mb-1">Danh sách d�9ch vụ</h4>
                        <span className="text-muted">Loại hình: {labels[businessType] || 'Đang tải...'}</span>
                    </div>
                    <Button variant="success" disabled={!businessType} onClick={openAddService}>+ Thêm d�9ch vụ</Button>
                </div>
                <Card className="border-0 shadow-sm">
                    <Table responsive hover className="m-0 align-middle">
                        <thead className="table-secondary">
                            <tr>
                                <th>ID</th>
                                <th>Tên d�9ch vụ</th>
                                <th>Loại hình</th>
                                {!isHotel && <th>Giá</th>}
                                <th className="text-center">Hành ��"ng</th>
                            </tr>
                        </thead>
                        <tbody>
                            {services.length > 0 ? services.map((service) => (
                                <tr key={service.id}>
                                    <td>#{service.id}</td>
                                    <td className="fw-bold">{serviceName(service)}</td>
                                    <td><span className="badge bg-info text-dark">{labels[businessType]}</span></td>
                                    {!isHotel && <td className="text-primary fw-bold">{Number(service.price || 0).toLocaleString()} VNĐ</td>}
                                    <td className="text-center">
                                        {isHotel && <Button variant="success" size="sm" className="me-2" onClick={() => openRoomModal(service)}>Thêm phòng</Button>}
                                        <Button variant="warning" size="sm" className="me-2" onClick={() => openEditService(service)}>Sửa</Button>
                                        <Button variant="danger" size="sm" onClick={() => deleteService(service.id)}>Xóa</Button>
                                    </td>
                                </tr>
                            )) : (
                                <tr><td colSpan={isHotel ? 4 : 5} className="text-center p-4 text-muted">Chưa có d�9ch vụ nào.</td></tr>
                            )}
                        </tbody>
                    </Table>
                </Card>
            </div>

            <Modal show={showServiceModal} onHide={closeServiceModal} centered size="lg">
                <Modal.Header closeButton><Modal.Title className="fw-bold">{editingService ? 'Sửa d�9ch vụ' : 'Thêm d�9ch vụ m�:i'}</Modal.Title></Modal.Header>
                <Form onSubmit={saveService}>
                    <Modal.Body>{serviceFields()}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={closeServiceModal}>Hủy</Button>
                        <Button type="submit" variant="success">Lưu lại</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            <Modal show={showRoomModal} onHide={closeRoomModal} centered size="lg">
                <Modal.Header closeButton><Modal.Title className="fw-bold">Thêm phòng m�:i</Modal.Title></Modal.Header>
                <Form onSubmit={saveRoom}>
                    <Modal.Body>{roomFields}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={closeRoomModal}>Hủy</Button>
                        <Button type="submit" variant="success">Lưu phòng</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
};

export default ProviderServices;
