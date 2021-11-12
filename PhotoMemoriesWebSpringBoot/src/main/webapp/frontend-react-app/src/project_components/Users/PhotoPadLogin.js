import React, {Component} from 'react';
import {Alert, Button, Card, Col, Form, Row} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSignInAlt, faUndo} from "@fortawesome/free-solid-svg-icons";
import {connect} from "react-redux";
import {authenticateUser} from "../services/index";

class PhotoPadLogin extends Component {
    constructor(props){
        super(props);
        this.state = this.initialState;
    }

    initialState = {
        email:'', password:'', error:''
    };

    detailsChange = event => {
        this.setState({
            [event.target.name] : event.target.value
        });
    };

    validateUser = () => {
        this.props.authenticateUser(this.state.email, this.state.password);
        setTimeout(() => {
            if (this.props.auth.isLoggedIn) {
                return this.props.history.push("/");
            } else {
                this.resetPhotoPadLoginForm();
                this.setState({"error": "Invalid email and password"});
            }
        }, 500);
    };

    resetPhotoPadLoginForm = () => {
      this.setState(() => this.initialState);
    };

    render() {
        const {email, password, error} = this.state;
        return (
            <Row className="justify-content-md-center">
                <Col xs={4}>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Card className={"border border-white bg-white text-dark"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faSignInAlt}/>  Login
                        </Card.Header>
                        <Card.Body>
                            <Form>
                                <Form.Group className="mb-3" controlId="formBasicEmail">
                                    <Form.Label>Email address</Form.Label>
                                    <Form.Control type="email" name="email" values={email} onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter email here" />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicPassword">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control type="password" name="password" values={password} onChange={this.detailsChange} className={"bg-white text-dark"} placeholder="Enter password here" />
                                </Form.Group>
                            </Form>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="button" variant="info" onClick={this.resetPhotoPadLoginForm} disabled={this.state.email.length === 0 && this.state.password.length === 0 && this.state.error.length === 0}>
                                <FontAwesomeIcon icon={faUndo}/>  Reset
                            </Button> {' '}
                            <Button size="sm" type="button" variant="success" onClick={this.validateUser} disabled={this.state.email.length === 0 || this.state.password.length === 0}>
                                <FontAwesomeIcon icon={faSignInAlt}/>  Login
                            </Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        );
    }
}

const mapStateToProps = state => {
    return {
        auth: state.auth
    }
};

const mapDispatchToProps = dispatch => {
    return {
        authenticateUser: (email, password) => dispatch(authenticateUser(email, password))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PhotoPadLogin);