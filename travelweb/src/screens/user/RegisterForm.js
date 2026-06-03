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
        role: "CUSTOMER"
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

            formData.append("fullName", user.fullName);
            formData.append("full_name", user.fullName);

            if (avatar) {
                formData.append("avatarFile", avatar);
            }

            await Apis.post(endpoints["register"], formData);
            alert("Đăng ký tài khoản thành công! Vui lòng đăng nhập");

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

            <Button type="submit" variant="success" className="w-100" disabled={loading}>
                {loading ? "Đang đăng ký..." : "Đăng ký"}
            </Button>
        </Form>
    );
};

export default RegisterForm;