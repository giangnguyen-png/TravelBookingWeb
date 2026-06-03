import { useState } from "react";
import { Alert, Button, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";

const RegisterForm = () => {
    const navigate = useNavigate();
    const [user, setUser] = useState({
    fullName: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "CUSTOMER",
    companyName: "",
    businessType: "TOUR_COMPANY"
});
    const [avatar, setAvatar] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const change = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    };

    const submit = async (e) => {
        e.preventDefault();
        setError("");

        if (user.password !== user.confirmPassword) {
            setError("Mật khẩu xác nhận không khớp.");
            return;
        }

        try {
            setLoading(true);
            const formData = new FormData();


            formData.append("username", user.username);
            formData.append("password", user.password);
            formData.append("fullName", user.fullName);
            formData.append("email", user.email);
            formData.append("role", user.role);

            if (user.role === "PROVIDER") {
                if (!user.companyName.trim()) {
                    setError("Vui lòng nhập tên công ty.");
                    setLoading(false);
                    return;
                }
                formData.append("companyName", user.companyName);
                formData.append("businessType", user.businessType);
            }

            if (avatar) {
                formData.append("avatarFile", avatar);
            }

            await Apis.post(endpoints["register"], formData);


            if (user.role === "PROVIDER") {
                alert("Đăng ký tài khoản Nhà cung cấp thành công! Vui lòng chờ Admin phê duyệt để sử dụng.");
            } else {
                alert("Đăng ký tài khoản thành công! Vui lòng đăng nhập");
            }

            navigate("/");
        } catch (err) {
            console.error("Chi tiết lỗi:", err.response?.data);
            setError(err.response?.data || "Đăng ký thất bại.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Form onSubmit={submit}>
            {error && <Alert variant="danger">{error}</Alert>}

            <Form.Group className="mb-3">
                <Form.Label>Họ và tên</Form.Label>
                <Form.Control
                    name="fullName"
                    type="text"
                    value={user.fullName}
                    onChange={change}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Tên đăng nhập</Form.Label>
                <Form.Control
                    name="username"
                    type="text"
                    value={user.username}
                    onChange={change}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Email</Form.Label>
                <Form.Control
                    name="email"
                    type="email"
                    value={user.email}
                    onChange={change}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Mật khẩu</Form.Label>
                <Form.Control
                    name="password"
                    type="password"
                    value={user.password}
                    onChange={change}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Xác nhận mật khẩu</Form.Label>
                <Form.Control
                    name="confirmPassword"
                    type="password"
                    value={user.confirmPassword}
                    onChange={change}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Ảnh đại diện</Form.Label>
                <Form.Control
                    type="file"
                    accept="image/*"
                    onChange={(e) => setAvatar(e.target.files[0])}
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Vai trò</Form.Label>
                <div>
                    <Form.Check
                        inline
                        type="radio"
                        name="role"
                        label="Khách hàng"
                        value="CUSTOMER"
                        checked={user.role === "CUSTOMER"}
                        onChange={change}
                    />
                    <Form.Check
                        inline
                        type="radio"
                        name="role"
                        label="Nhà cung cấp"
                        value="PROVIDER"
                        checked={user.role === "PROVIDER"}
                        onChange={change}
                    />
                </div>
            </Form.Group>

            {user.role === "PROVIDER" && (
                <div className="p-3 mb-3 bg-light rounded border">
                    <h6 className="text-primary mb-3">Thông tin hồ sơ Nhà cung cấp</h6>

                    <Form.Group className="mb-3">
                        <Form.Label>Tên công ty / Thương hiệu</Form.Label>
                        <Form.Control
                            name="companyName"
                            type="text"
                            placeholder="Ví dụ: Khách sạn Mường Thanh, Vietravel..."
                            value={user.companyName}
                            onChange={change}
                            required
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Loại hình kinh doanh dịch vụ</Form.Label>
                        <Form.Select
                            name="businessType"
                            value={user.businessType}
                            onChange={change}
                        >
                            <option value="TOUR_COMPANY">Kinh doanh Tour du lịch</option>
                            <option value="HOTEL">Kinh doanh Phòng khách sạn</option>
                            <option value="AIRLINE">Kinh doanh Vé máy bay (Hãng hàng không)</option>
                            <option value="BUS_COMPANY">Kinh doanh Vé xe khách (Nhà xe)</option>
                        </Form.Select>
                    </Form.Group>
                </div>
            )}

            <Button type="submit" variant="success" className="w-100" disabled={loading}>
                {loading ? "Đang đăng ký..." : "Đăng ký"}
            </Button>
        </Form>
    );
};

export default RegisterForm;