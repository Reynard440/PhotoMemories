import React, {Component} from 'react';
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAddressBook, faBook, faFill, faIdBadge, faPhone, faUndo, faUserEdit} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";

export default class PhotoPadUpdateUser extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.detailsChange = this.detailsChange.bind(this);
    };

    initialState = {
        userId:'', fName:'', lName:'', phone:'', email:''
    };

    componentDidMount() {
        this.retrieveById();
    };

    retrieveById = () => {
        axios.get("http://localhost:8095/photo-memories/mvc/v1/c1/getUserByEmail/reynardengels@gmail.com")
            .then(res => {
                console.log(res);
                if (res.data !== null) {
                    this.setState({
                        userId: res.data.cargo.userId,
                        fName: res.data.cargo.firstName,
                        lName: res.data.cargo.lastName,
                        phone: res.data.cargo.phoneNumber,
                        email: res.data.cargo.email
                    });
                }
            }).catch((error) => {
            console.log("Error - " +error);
        });
    };

    detailsChange = event => {
        this.setState({
            [event.target.name] : event.target.value
        });
    };

    render() {
        const {userId, fName, lName, phone, email} = this.state;
        return (
            <div>
                <Row className="justify-content-md-center">
                    <Col xs={5}>
                        <Card className={"border border-white bg-white text-dark"}>
                            <Card.Header>
                                <FontAwesomeIcon icon={faUserEdit}/>  Update Account
                            </Card.Header>
                            <Card.Body>
                                <Form>
                                    <Form.Group className="mb-3" controlId="formBasicEmail">
                                        <Form.Label><FontAwesomeIcon icon={faAddressBook}/> Email address</Form.Label>
                                        <Form.Control type="email" name="email" values="email" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter email here" />
                                    </Form.Group>

                                    <Form.Group className="mb-3" controlId="formBasicFirstName">
                                        <Form.Label><FontAwesomeIcon icon={faFill}/> First Name</Form.Label>
                                        <Form.Control type="text" name="fName" values="fName" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter first name here" />
                                    </Form.Group>

                                    <Form.Group className="mb-3" controlId="formBasicLastName">
                                        <Form.Label><FontAwesomeIcon icon={faBook}/> Last Name</Form.Label>
                                        <Form.Control type="text" name="lName" values="lName" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter last name here" />
                                    </Form.Group>

                                    <Form.Group className="mb-3" controlId="formBasicCellphone">
                                        <Form.Label><FontAwesomeIcon icon={faPhone}/> Cellphone Number</Form.Label>
                                        <Form.Control type="cell" name="phone" values="phone" onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter cellphone here" />
                                    </Form.Group>

                                </Form>
                            </Card.Body>
                            <Card.Footer style={{"textAlign":"right"}}>
                                <Button size="sm" type="button" variant="info"  >
                                    <FontAwesomeIcon icon={faUndo}/>  Reset
                                </Button> {' '}
                                <Button size="sm" type="button" variant="success" >
                                    <FontAwesomeIcon icon={faIdBadge}/>  Update
                                </Button>
                            </Card.Footer>
                        </Card>
                    </Col>
                </Row>
            </div>
        );
    }
};