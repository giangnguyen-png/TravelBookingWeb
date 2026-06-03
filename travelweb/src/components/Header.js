import { useContext } from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import cookies from 'react-cookies';
import { MyDispatchContext, MyUserContext } from '../configs/MyContext';

const Header = () => {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const navigate = useNavigate();
  const location = useLocation();

  const logout = () => {
    cookies.remove('token', { path: '/' });
    cookies.remove('user', { path: '/' });
    dispatch({ type: 'logout' });
    navigate('/user');
  };

  const renderAuthButton = () => {
    if (user) {
      return (
        <div className="d-flex align-items-center">
          <Link
            to="/user"
            className="me-3 fw-bold text-primary text-decoration-none"
            style={{ cursor: 'pointer' }}
          >
            Xin chào, {user.fullName || user.username}
          </Link>
          <button
            onClick={logout}
            className="btn btn-outline-danger btn-sm px-3"
            style={{ borderRadius: '8px' }}
          >
            Đăng xuất
          </button>
        </div>
      );
    }

    if (location.pathname === '/user') {
      return null;
    }

    return (
      <Link
        to="/user"
        className="btn btn-primary px-4"
        style={{ borderRadius: '8px', color: '#fff', textDecoration: 'none' }}
      >
        Đăng nhập
      </Link>
    );
  };

  return (
    <Navbar bg="light" expand="lg" className="mb-3 shadow-sm">
      <Container>
        <Navbar.Brand as={Link} to="/">
          TravelBooking
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="main-navbar" />
        <Navbar.Collapse id="main-navbar">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">
              Trang chủ
            </Nav.Link>
            <Nav.Link as={Link} to="/compare">
              So sánh dịch vụ
            </Nav.Link>
          </Nav>
          <Nav className="align-items-center">
            {renderAuthButton()}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
