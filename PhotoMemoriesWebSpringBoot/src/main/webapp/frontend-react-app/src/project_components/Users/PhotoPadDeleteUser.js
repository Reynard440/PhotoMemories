import React, {Component} from 'react';
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTrash, faUndo, faUsers} from "@fortawesome/free-solid-svg-icons";

export default class PhotoPadDeleteUser extends Component {
    render() {
        return (
            <div>
                <Row className="justify-content-md-center">
                    <Col xs={5}>
                        <Card className={"border border-white bg-white text-dark"}>
                            <Card.Header>
                                <FontAwesomeIcon icon={faUsers}/>  Delete Account
                            </Card.Header>
                            <Card.Body>
                                <Form>
                                    <Form.Group className="mb-3" controlId="formBasicEmail">
                                        <Form.Label>Confirm Your Email address</Form.Label>
                                        <Form.Control type="email" name="email" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter email here" />
                                    </Form.Group>
                                </Form>
                            </Card.Body>
                            <Card.Footer style={{"textAlign":"right"}}>
                                <Button size="sm" type="button" variant="info">
                                    <FontAwesomeIcon icon={faUndo}/>  Reset
                                </Button> {' '}
                                <Button size="sm" type="button" variant="danger" >
                                    <FontAwesomeIcon icon={faTrash}/>  Delete
                                </Button>
                            </Card.Footer>
                        </Card>
                    </Col>
                </Row>
            </div>
        );
    }
};