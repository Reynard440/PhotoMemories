import React, {Component} from 'react';
import {Button, Card, Col, Form, Row} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSignInAlt, faUndo} from "@fortawesome/free-solid-svg-icons";

export default class PhotoPadLogin extends Component {
    constructor(props){
        super(props);
        this.state = this.initialState;
    }

    initialState = {
        email:'', password:''
    };

    detailsChange = event => {
        this.setState({
            [event.target.name] : event.target.value
        });
    };

    resetPhotoPadLoginForm = () => {
      this.setState(() => this.initialState);
    };

    render() {
        const {email, password} = this.state;
        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    <Card className={"border border-white bg-white text-dark"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faSignInAlt}/>  Login
                        </Card.Header>
                        <Card.Body>
                            <Form>
                                <Form.Group className="mb-3" controlId="formBasicEmail">
                                    <Form.Label>Email address</Form.Label>
                                    <Form.Control type="email" name="email" values="email" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter email here" />
                                    <Form.Text className="text-muted">
                                        We'll never share your email with anyone else.
                                    </Form.Text>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicPassword">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control type="password" name="password" values="password" onChange={this.detailsChange} className={"bg-white text-dark"} placeholder="Enter password here" />
                                </Form.Group>
                            </Form>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="button" variant="info" onClick={this.resetPhotoPadLoginForm} disabled={this.state.email.length === 0 || this.state.password.length === 0}>
                                <FontAwesomeIcon icon={faUndo}/>  Reset
                            </Button> {' '}
                            <Button size="sm" type="button" variant="success" disabled={this.state.email.length === 0 || this.state.password.length === 0}>
                                <FontAwesomeIcon icon={faSignInAlt}/>  Login
                            </Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        );
    }
}