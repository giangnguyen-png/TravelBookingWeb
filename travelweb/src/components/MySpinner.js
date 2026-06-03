import { Spinner } from "react-bootstrap";

const MySpinner = () => {
    return (
        <div className="d-flex justify-content-center my-3">
            <Spinner animation="border" variant="primary" />
        </div>
    );
};

export default MySpinner;