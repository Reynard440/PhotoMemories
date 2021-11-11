import React, {Component} from 'react';
import PhotoPadToast from "./PhotoPadToast";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBackward, faEdit, faPlusSquare, faUndo} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";

export default class PhotoPadEdit extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state.show = false;
        this.photoChanged = this.photoChanged.bind(this);
        this.updatePhoto = this.updatePhoto.bind(this);
    }

    initialState = {
        photoId:'', ph_name:'', location:'', ph_captured:'', email:''
    };

    componentDidMount() {
        const photoId = +this.props.match.params.photoId;
        if (photoId) {
            this.retrieveById(photoId);
        }
    };

    retrieveById = (photoId) => {
        axios.get("http://localhost:8095/photo-memories/mvc/v1/c2/getPhotoById/"+photoId)
            .then(res => {
                if (res.data !== null) {
                    this.setState({
                        photoId: res.data.cargo.photoId,
                        ph_name: res.data.cargo.photoName,
                        location: res.data.cargo.photoLocation,
                        ph_captured: res.data.cargo.photoCapturedBy
                    });
                }
            }).catch((error) => {
            console.log("Error - " +error);
        });
    };

    updatePhoto = event => {
        const bodyInfo = new FormData();
        bodyInfo.append("pName", this.state.ph_name);
        bodyInfo.append("pLocation", this.state.location);
        bodyInfo.append("pCaptured", this.state.ph_captured);
        bodyInfo.append("email", this.state.email);


        axios.put("http://localhost:8095/photo-memories/mvc/v1/c2/updateMetadata/" + this.state.photoId, bodyInfo,
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
    }

    photoChanged = event => {
        this.setState({
            [event.target.name] : event.target.value
        });
    };

    photoGallery = () => {
        return this.props.history.push("/gallery");
    };


    clearAllFields = () => {
        this.setState(() => this.initialState);
    };

    render() {
        const {ph_name, location, ph_captured, email} = this.state;
        return (
            <div>
                <div style={{"display": this.state.show ? "block": "none"}}>
                    <PhotoPadToast show={this.state.show} message={"Photo updated, you can now share it with the group."} type={"success"}/>
                </div>
                <Card className={"border border-white bg-white text-dark"}>
                    <CardHeader><FontAwesomeIcon icon={faPlusSquare}/> Update a Photo</CardHeader>
                    <Form onReset={this.clearAllFields} onSubmit={this.updatePhoto} id={"photoEditForm"}>
                        <Card.Body>
                            <Row>
                                <Form.Group as={Col} controlId="formGridPhotoName">
                                    <Form.Label>Photo Name</Form.Label>
                                    <Form.Control type="text" name="ph_name" value={ph_name} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of photo date" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridLocation">
                                    <Form.Label>Photo Location</Form.Label>
                                    <Form.Control type="text" name="location" value={location} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of city" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridCaptured">
                                    <Form.Label>Capture By?</Form.Label>
                                    <Form.Control required type="text" name="ph_captured" value={ph_captured} onChange={this.photoChanged} autoComplete="off" placeholder="Enter name of person" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridEmail">
                                    <Form.Label>User email</Form.Label>
                                    <Form.Control type="email" name="email" value={email} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter your email" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"right" }}>
                            <Button size="md" type="reset" variant="info" onClick={this.clearAllFields}>
                                <FontAwesomeIcon icon={faUndo}/> Clear
                            </Button>{' '}
                            <Button size="md" type="submit" variant="success" disabled={this.state.email.length === 0 || this.state.location.length === 0 || this.state.ph_captured.length === 0 || this.state.ph_name.length === 0} onClick={this.updatePhoto}>
                                <FontAwesomeIcon icon={faEdit}/> Update Photo
                            </Button>{' '}
                            <Button size="md" type="button" variant="primary" onClick={this.photoGallery.bind()}>
                                <FontAwesomeIcon icon={faBackward}/> Photo Gallery
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
}