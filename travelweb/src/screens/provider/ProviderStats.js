import React from 'react';
import { Card, Col, Form, Row } from 'react-bootstrap';
import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';

const ProviderStats = ({ stats, monthlyData, yearlyData, timeType, setTimeType }) => (
    <div>
        <h3 className="mb-4 fw-bold text-dark">Báo cáo & Th�ng kê doanh thu</h3>
        <Row className="mb-4">
            <Col md={6} className="mb-3">
                <Card className="p-3 shadow-sm border-0 bg-white">
                    <Card.Body>
                        <h6 className="text-muted text-uppercase fw-semibold small">T�"ng s� lượt �ặt d�9ch vụ</h6>
                        <h3 className="text-info fw-bold mt-2">
                            {stats.bookings || 0} <span className="fs-6 fw-normal text-muted">lượt</span>
                        </h3>
                    </Card.Body>
                </Card>
            </Col>
            <Col md={6} className="mb-3">
                <Card className="p-3 shadow-sm border-0 bg-white">
                    <Card.Body>
                        <h6 className="text-muted text-uppercase fw-semibold small">T�"ng doanh thu</h6>
                        <h3 className="text-primary fw-bold mt-2">
                            {(stats.revenue || 0).toLocaleString()} <span className="fs-6 fw-normal text-muted">VNĐ</span>
                        </h3>
                    </Card.Body>
                </Card>
            </Col>
        </Row>

        <Card className="shadow-sm border-0 bg-white p-4 mb-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="fw-bold m-0 text-secondary">
                    {timeType === 'MONTH' ? 'BiỒu �� doanh thu theo các tháng' : 'BiỒu �� doanh thu qua các nĒm'}
                </h5>
                <Form.Select
                    style={{ width: '220px' }}
                    value={timeType}
                    onChange={(e) => setTimeType(e.target.value)}
                    className="form-select-sm shadow-none"
                >
                    <option value="MONTH">Th�ng kê theo Tháng</option>
                    <option value="YEAR">Th�ng kê theo NĒm</option>
                </Form.Select>
            </div>

            <div style={{ width: '100%', height: 320 }}>
                <ResponsiveContainer>
                    <BarChart
                        data={timeType === 'MONTH' ? monthlyData : yearlyData}
                        margin={{ top: 10, right: 30, left: 20, bottom: 5 }}
                    >
                        <CartesianGrid strokeDasharray="3 3" vertical={false} />
                        <XAxis dataKey={timeType === 'MONTH' ? 'month' : 'year'} tickFormatter={(v) => timeType === 'MONTH' ? `Tháng ${v}` : `NĒm ${v}`} />
                        <YAxis />
                        <Tooltip formatter={(value) => [Number(value).toLocaleString() + ' VNĐ', 'Doanh thu']} />
                        <Legend />
                        <Bar dataKey="revenue" name="Doanh thu" fill="#0d6efd" radius={[4, 4, 0, 0]} />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </Card>
    </div>
);

export default ProviderStats;
