// src/screens/user/LoginForm.js

import { useState } from "react";
import { Form, Button } from "react-bootstrap";

const LoginForm = ({ onLogin }) => {
    const [user, setUser] = useState({
        username: "",
        password: ""
    });

    const fields = [
        {
            field: "username",
            label: "Tên đăng nhập",
            type: "text"
        },
        {
            field: "password",
            label: "Mật khẩu",
            type: "password"
        }
    ];

    const change = (e, field) => {
        setUser({
            ...user,
            [field]: e.target.value
        });
    };

    const submit = (e) => {
        e.preventDefault();
        onLogin(user);
    };

    return (
        <Form onSubmit={submit}>
            {fields.map(f => (
                <Form.Group className="mb-3" key={f.field}>
                    <Form.Label>{f.label}</Form.Label>
                    <Form.Control
                        type={f.type}
                        value={user[f.field]}
                        onChange={(e) => change(e, f.field)}
                    />
                </Form.Group>
            ))}

            <Button type="submit" variant="primary" className="w-100">
                Đăng nhập
            </Button>
        </Form>
    );
};

export default LoginForm;