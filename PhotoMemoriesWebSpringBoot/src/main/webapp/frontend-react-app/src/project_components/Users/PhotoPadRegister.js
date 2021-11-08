import React, {Component} from 'react';
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faAddressBook,
    faBook,
    faFill,
    faIdBadge,
    faKey,
    faPassport,
    faPhone,
    faUndo
} from "@fortawesome/free-solid-svg-icons";

export default  class PhotoPadRegister extends Component {
    render() {
        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    <Card className={"border border-white bg-white text-dark"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faKey}/>  Register
                        </Card.Header>
                        <Card.Body>
                            <Form>
                                <Form.Group className="mb-3" controlId="formBasicEmail">
                                    <Form.Label><FontAwesomeIcon icon={faAddressBook}/> Email address</Form.Label>
                                    <Form.Control type="email" name="email" values="email" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter email here" />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicFirstName">
                                    <Form.Label><FontAwesomeIcon icon={faFill}/> First Name</Form.Label>
                                    <Form.Control type="text" name="fname" values="fname" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter first name here" />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicLastName">
                                    <Form.Label><FontAwesomeIcon icon={faBook}/> Last Name</Form.Label>
                                    <Form.Control type="text" name="lname" values="lname" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter last name here" />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicCellphone">
                                    <Form.Label><FontAwesomeIcon icon={faPhone}/> Cellphone Number</Form.Label>
                                    <Form.Control type="cell" name="cellphone" values="cellphone" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter cellphone here" />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formBasicPassword">
                                    <Form.Label><FontAwesomeIcon icon={faPassport}/> Password</Form.Label>
                                    <Form.Control type="password" name="password" values="password" onChange={this.detailsChange} className={"bg-white text-dark"} placeholder="Enter password here" />
                                </Form.Group>
                            </Form>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="button" variant="info"  >
                                <FontAwesomeIcon icon={faUndo}/>  Reset
                            </Button> {' '}
                            <Button size="sm" type="button" variant="success" >
                                <FontAwesomeIcon icon={faIdBadge}/>  Register
                            </Button>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        );
    }
}