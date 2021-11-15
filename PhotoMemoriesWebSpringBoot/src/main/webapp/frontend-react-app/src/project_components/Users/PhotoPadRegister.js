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
import {connect} from "react-redux";
import {addUser} from "../services/index";
import PhotoPadToast from "../Photos/PhotoPadToast";

class PhotoPadRegister extends Component {
    constructor(props){
        super(props);
        this.state = this.initialState;
        this.detailsChange = this.detailsChange.bind(this);
        this.resetRegister = this.resetRegister.bind(this);
        this.state.show  = false;
        this.state.message = '';
    }

    initialState = {
        email:'', password:'', fname:'', lname:'', cellphone:'', error:''
    };

    componentDidMount() {
        this.resetRegister();
    };

    detailsChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    createNewUser = () => {

        const newUser = new FormData();
        newUser.append("email", this.state.email);
        newUser.append("fname", this.state.fname);
        newUser.append("lname", this.state.lname);
        newUser.append("password", this.state.password);
        newUser.append("cellphone", this.state.cellphone);


        this.props.addUser(newUser);
        this.resetRegister();
        this.props.history.push("/login");
    };

    resetRegister = () => {
        this.setState(() => this.initialState);
        // this.state.name;
        console.log(this.state.value);
    };

    render() {
        const {email, password, fname, lname, cellphone} = this.state;
        return (
            <Row className="justify-content-center align-content-center">
                <Col lg={5}>
                    <div style={{"display": this.state.show ? "block": "none"}}>
                        <PhotoPadToast show={this.state.show} message="Added successfully" type={"success"}/>
                    </div>
                    <Card className={"border border-white bg-white text-dark"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faKey}/>  Register
                        </Card.Header>
                        <Card.Body>
                            <Form onReset={this.resetRegister} onSubmit={this.createNewUser} controlId="registerForm">
                                <Form.Group className="mb-2" controlId="formBasicEmail">
                                    <Form.Label><FontAwesomeIcon icon={faAddressBook}/> Email address</Form.Label>
                                    <Form.Control type="email" name="email" value={email} onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter email here" />
                                </Form.Group>

                                <Form.Group className="mb-2" controlId="formBasicFirstName">
                                    <Form.Label><FontAwesomeIcon icon={faFill}/> First Name</Form.Label>
                                    <Form.Control type="text" name="fname" value={fname} onChange={this.detailsChange} className={"bg-white text-dark"} placeholder="Enter first name here" />
                                </Form.Group>

                                <Form.Group className="mb-2" controlId="formBasicLastName">
                                    <Form.Label><FontAwesomeIcon icon={faBook}/> Last Name</Form.Label>
                                    <Form.Control type="text" name="lname" value={lname} onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter last name here" />
                                </Form.Group>

                                <Form.Group className="mb-2" controlId="formBasicCellphone">
                                    <Form.Label><FontAwesomeIcon icon={faPhone}/> Cellphone Number</Form.Label>
                                    <Form.Control type="cell" name="cellphone" value={cellphone} onChange={this.detailsChange}  className={"bg-white text-dark"} placeholder="Enter cellphone here" />
                                </Form.Group>

                                <Form.Group className="mb-2" controlId="formBasicPassword">
                                    <Form.Label><FontAwesomeIcon icon={faPassport}/> Password</Form.Label>
                                    <Form.Control type="password" name="password" value={password} onChange={this.detailsChange} className={"bg-white text-dark"} placeholder="Enter password here" />
                                </Form.Group>
                            </Form>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" type="button" variant="info" onClick={this.resetRegister} >
                                <FontAwesomeIcon icon={faUndo}/>  Reset
                            </Button> {' '}
                            <Button size="sm" type="button" variant="success" onClick={this.createNewUser} disabled={this.state.email.length === 0 || this.state.password.length === 0 || this.state.fname.length === 0 || this.state.lname.length === 0 || this.state.cellphone.length === 0}>
                                <FontAwesomeIcon icon={faIdBadge}/>  Register
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
        user: state.user
    }
};

const mapDispatchToProps = dispatch => {
    return {
        addUser: (newUser) => dispatch(addUser(newUser))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PhotoPadRegister);