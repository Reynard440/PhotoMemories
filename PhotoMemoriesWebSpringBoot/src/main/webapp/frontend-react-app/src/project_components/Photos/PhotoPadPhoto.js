import React, {Component} from "react";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare, faSave, faUndo} from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import PhotoPadToast from "./PhotoPadToast";

export default  class PhotoPadPhoto extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state.show = false;
        this.photoChanged = this.photoChanged.bind(this);
        this.addPhoto = this.addPhoto.bind(this);
    }

    initialState = {
        modifiedDate:'', ph_name:'', location:'', ph_captured:'', email:'', photo:''
    };

    clearAllFields = () => {
        this.setState(() => this.initialState);
    };

    photoList = () => {
        return this.props.history.push("/list");
    };

    addPhoto = event => {
        event.preventDefault();

        const bodyInfo = new FormData();
        bodyInfo.append("modifiedDate", this.state.modifiedDate);
        bodyInfo.append("photoName", this.state.ph_name);
        bodyInfo.append("photoLocation", this.state.location);
        bodyInfo.append("photoCapturedBy", this.state.ph_captured);
        bodyInfo.append("email", this.state.email);
        bodyInfo.append("photo", this.state.photo);

        axios.post("http://localhost:8095/photo-memories/mvc/v1/c2/addNewPhoto", bodyInfo,
            {
                headers:{
                    "Access-Control-Allow-Origin": "*",
                    "Authorization": "Carier eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZXluYXJkZW5nZWxzQGdtYWlsLmNvbSIsInJvbGVzIjpbIlVTRVJfUk9MRSJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwOTUvcGhvdG8tbWVtb3JpZXMvbXZjL2xvZ2luIiwiZXhwIjoxNjM2MDIxMTk2fQ.6lvWZ4NtC9r3fvJWa72W3sRtk2rFJauBHioQkuoOyTg"
                }
            })
            .then(res => {
                if (res.data != null) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show": false}), 3000);
                    setTimeout(() => this.photoList(), 3000);
                } else {
                    this.setState({"show": false});
                }
            })
            .catch(error => {
                console.log(error.response)
            });

        this.setState(this.initialState);
    };

    photoChanged = (event) => {
        if (event.target.name === "photo") {
            this.state.photo = event.target.files[0];
        } else {
            this.setState({
                [event.target.name] : event.target.value
            });
        }
    };

    render(){
        const {modifiedDate, ph_name, location, ph_captured, email, photo} = this.state;
        return (
            <div>
                <div style={{"display": this.state.show ? "block": "none"}}>
                    <PhotoPadToast show={this.state.show} message={"Photo saved, you can now share it with the group."} type={"success"}/>
                </div>
                <Card className={"border border-white bg-white text-dark"}>
                    <CardHeader><FontAwesomeIcon icon={faPlusSquare}/> {this.state.photoId ? "Update a Photo":"Add a Photo"}</CardHeader>
                    <Form onReset={this.clearAllFields} onSubmit={this.addPhoto} id={"photoForm"}>
                        <Card.Body>
                            <Row>
                                <Form.Group as={Col} controlId="formGridModifiedDate">
                                    <Form.Label>Date Modified</Form.Label>
                                    <Form.Control type="date" name="modifiedDate" value={modifiedDate} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter modified date" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridPhotoName">
                                    <Form.Label>Photo Name</Form.Label>
                                    <Form.Control type="text" name="ph_name" value={ph_name} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of photo date" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridLocation">
                                    <Form.Label>Photo Location</Form.Label>
                                    <Form.Control type="text" name="location" value={location} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of city" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCaptured">
                                    <Form.Label>Capture By?</Form.Label>
                                    <Form.Control required type="text" name="ph_captured" value={ph_captured} onChange={this.photoChanged} autoComplete="off" placeholder="Enter name of person" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridEmail">
                                    <Form.Label>User email</Form.Label>
                                    <Form.Control type="email" name="email" value={email} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter your email" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridPhoto">
                                    <Form.Label>Import the photo here...</Form.Label>
                                    <Form.Control type="file" name="photo" value={photo} onChange={this.photoChanged} required autoComplete="off" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"right" }}>
                            <Button size="md" type="reset" variant="info">
                                <FontAwesomeIcon icon={faUndo}/> Clear
                            </Button>{' '}
                            <Button size="md" type="submit" variant="success" disabled={this.state.email.length === 0 || this.state.ph_name.length === 0 || this.state.ph_captured.length === 0 || this.state.location.length === 0}  onClick={this.addPhoto}>
                                <FontAwesomeIcon icon={faSave}/> Add Photo
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
}