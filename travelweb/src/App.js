import { useReducer } from 'react';
import { Container } from 'react-bootstrap';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import cookies from 'react-cookies';
import { MyDispatchContext, MyUserContext } from './configs/MyContext';
import Home from './screens/home/Home';
import User from './screens/user/User';
import Header from './components/Header';
import Footer from './components/Footer';
import ServiceDetail from './screens/services/ServiceDetail';
import ServiceBooking from './screens/services/ServiceBooking';
import ProviderDashboard from './screens/provider/ProviderDashboard';
import CompareServices from './screens/home/CompareServices';
import 'bootstrap/dist/css/bootstrap.min.css';

function myReducer(state, action) {
  switch (action.type) {
    case 'login':
      return action.payload;
    case 'logout':
      return null;
    default:
      return state;
  }
}

const AppContent = () => {
  const location = useLocation();
  const isProviderPage = location.pathname.startsWith('/provider');

  return (
    <Container fluid={isProviderPage}>
      {!isProviderPage && <Header />}

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/user" element={<User />} />
        <Route path="/services/:type/:id" element={<ServiceDetail />} />
        <Route path="/booking/:type/:id" element={<ServiceBooking />} />
        <Route path="/compare" element={<CompareServices />} />
        <Route path="/provider" element={<ProviderDashboard />} />
      </Routes>

      {!isProviderPage && <Footer />}
    </Container>
  );
};

function App() {

  const [user, dispatch] = useReducer(myReducer, cookies.load('user') || null);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>
          <AppContent />
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>
  );
}

export default App;
