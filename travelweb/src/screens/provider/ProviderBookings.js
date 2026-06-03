import React from 'react';
import { Card, Table } from 'react-bootstrap';

const formatCurrency = (value) => `${Number(value || 0).toLocaleString('vi-VN')} VNĐ`;

const formatBookingDate = (value) => {
    if (!value) return 'Chưa cập nhật';
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? value : date.toLocaleString('vi-VN');
};

const bookingCustomer = (booking) => booking.customerId || booking.customer || {};

const ProviderBookings = ({ providerBookings }) => (
    <div>
        <h4 className="fw-bold mb-3">Danh sách đơn đặt hàng</h4>
        <Card className="border-0 shadow-sm">
            <Table responsive hover className="m-0 align-middle">
                <thead className="table-secondary">
                    <tr>
                        <th>Mã đơn</th>
                        <th>Khách hàng</th>
                        <th>Tên đăng nhập</th>
                        <th>Email</th>
                        <th>Loại đặt</th>
                        <th>Trạng thái</th>
                        <th>Tổng tiền</th>
                        <th>Ngày đặt</th>
                    </tr>
                </thead>
                <tbody>
                    {providerBookings.length > 0 ? providerBookings.map((booking) => {
                        const customer = bookingCustomer(booking);
                        return (
                            <tr key={booking.id}>
                                <td>#{booking.id}</td>
                                <td className="fw-bold">{customer.fullName || customer.username || 'Khách hàng'}</td>
                                <td>{customer.username || '-'}</td>
                                <td>{customer.email || '-'}</td>
                                <td>{booking.bookingType}</td>
                                <td>
                                    <span className={`badge ${booking.status === 'PAID' || booking.status === 'COMPLETED' ? 'bg-success' : 'bg-warning text-dark'}`}>
                                        {booking.status}
                                    </span>
                                </td>
                                <td className="text-primary fw-bold">{formatCurrency(booking.totalPrice || booking.total_price)}</td>
                                <td>{formatBookingDate(booking.createdAt || booking.created_at)}</td>
                            </tr>
                        );
                    }) : (
                        <tr>
                            <td colSpan={8} className="text-center p-4 text-muted">Chưa có khách hàng nào đặt dịch vụ của bạn.</td>
                        </tr>
                    )}
                </tbody>
            </Table>
        </Card>
    </div>
);

export default ProviderBookings;
