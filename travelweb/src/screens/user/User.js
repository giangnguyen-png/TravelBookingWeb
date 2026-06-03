// src/screens/user/User.js
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Card, Tabs, Tab } from "react-bootstrap";
import cookies from "react-cookies";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import UserProfile from './UserProfile';
import { MyDispatchContext, MyUserContext } from '../../configs/MyContext';

const User = () => {
    const navigate = useNavigate();
    const dispatch = useContext(MyDispatchContext);
    const currentUser = useContext(MyUserContext);

    const login = async (data) => {
        try {
            let res = await Apis.post(endpoints["login"], data);
            
            // Lưu token (thêm path='/' để đồng bộ toàn dự án)
            cookies.save("token", res.data.token, { path: "/" });

            let profile = await authApis().get(endpoints["current-user"]);
            console.log(profile.data);
            
            const userData = profile.data;

            if (userData.role === "PROVIDER") {
                navigate("/provider");
            } else {
                navigate("/");
            }
            // console.log("THÔNG TIN USER TỪ BE TRẢ VỀ NÈ:", userData);

            // Lưu luôn biến user xuống cookie để App.js gọi lên khi F5
            cookies.save("user", userData, { path: "/" });

            dispatch({
                type: "login",
                payload: userData
            });

            // Điều hướng đúng chuẩn React (Vì đã xóa cái useEffect ngáng đường bên Header)
            if (userData.role === "PROVIDER") {
                navigate("/provider");
            } else {
                navigate("/");
            }
        } catch (err) {
            console.error(err);
        }
    };

    const logout = () => {
        cookies.remove("token", { path: "/" });
        cookies.remove("user", { path: "/" });
        dispatch({
            type: "logout"
        });
    };

    return (
        <Container className="mt-5">
            {currentUser === null ? (
                <Card className="mx-auto shadow-lg" style={{ maxWidth: "600px" }}>
                    <Card.Body>
                        <Tabs defaultActiveKey="login" className="mb-4">
                            <Tab eventKey="login" title="Đăng nhập">
                                <LoginForm onLogin={login} />
                            </Tab>
                            <Tab eventKey="register" title="Đăng ký">
                                <RegisterForm />
                            </Tab>
                        </Tabs>
                    </Card.Body>
                </Card>
            ) : (
                <UserProfile />
            )}
        </Container>
    );
};

export default User;