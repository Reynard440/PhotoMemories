import React, {Component} from 'react';
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBackward, faSave, faShareSquare, faUndo} from "@fortawesome/free-solid-svg-icons";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import axios from "axios";
import PhotoPadToast from "./PhotoPadToast";
import {getPhoto} from "../services";
import {connect} from "react-redux";

class PhotoPadSharePhoto extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state.show = false;
        this.detailsChanged = this.detailsChanged.bind(this);
        this.sharePhoto = this.sharePhoto.bind(this);
    }

    initialState = {
        photoId:+this.props.match.params.photoId, ph_access:false, recipientEmail:'', sendingEmail:''
    };

    componentDidMount() {
        const photoId = +this.props.match.params.photoId;
        console.log("hello");
        if (photoId) {
            this.retrieveById(photoId);
        }
    };

    retrieveById = (photoId) => {
        this.props.getPhoto(photoId);
        setTimeout(() => {
            let photo = this.props.photoObj.photos;
            if (photo != null) {
                this.setState({
                    photoId: photo.photoId
                });
            }
        },1500);
    };

    sharePhoto = event => {
        event.preventDefault();

        const bodyInfo = new FormData();
        bodyInfo.append("receivingEmail", this.state.recipientEmail);
        bodyInfo.append("accessRights", this.state.ph_access);
        bodyInfo.append("accessRights", this.state.ph_access);
        bodyInfo.append("id", this.state.photoId);
        bodyInfo.append("sharingEmail", this.state.sendingEmail);

        axios.post("http://localhost:8095/v1/c2/sharePhotoWithAnotherUser", bodyInfo,
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
                    setTimeout(() => this.photoGallery(), 3000);
                } else {
                    this.setState({"show": false});
                }
            })
            .catch(error => {
                console.log(error.response)
            });

        this.setState(this.initialState);
    };

    detailsChanged = event => {
        console.log(event.target.name);

        if (event.target.name === 'ph_access') {
            if (this.state.ph_access === false) {
                this.state.ph_access = true;
            } else {
                this.state.ph_access = false;
            }
            console.log(this.state.ph_access);
        } else {
            this.setState({
                [event.target.name] : event.target.value
            });
        }
    };

    photoGallery = () => {
        return this.props.history.push("/gallery");
    };

    clearAllFields = () => {
        this.setState(() => this.initialState);
    };

    render() {
        const {recipientEmail, sendingEmail, photoId, ph_access} = this.state;
        return (
            <div>
                <div style={{"display": this.state.show ? "block": "none"}}>
                    <PhotoPadToast show={this.state.show} message={"Photo shared, this photo was successfully shared with" + this.state.recipientEmail + "."} type={"info"}/>
                </div>
                <Card className={"border border-white bg-white text-dark"}>
                    <CardHeader><FontAwesomeIcon icon={faShareSquare}/> Share a Photo with Someone </CardHeader>
                    <Form onReset={this.clearAllFields} onSubmit={this.sharePhoto} id={"photoShareForm"}>
                        <Card.Body>
                            <Row>
                                <Form.Group as={Col} controlId="formGridToEmail">
                                    <Form.Label>To Email</Form.Label>
                                    <Form.Control type="email" name="recipientEmail" value={recipientEmail} onChange={this.detailsChanged} required autoComplete="off" placeholder="Enter destination email here" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridToEmail">
                                    <Form.Label>From Email</Form.Label>
                                    <Form.Control type="email" name="sendingEmail" value={sendingEmail} onChange={this.detailsChanged} required autoComplete="off" placeholder="Your email address" className={"bg-white text-dark"} />
                                </Form.Group>

                            </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridPhotoId">
                                    <Form.Label>Photo Id</Form.Label>
                                    <Form.Control type="text" name="photoId" value={photoId} onChange={this.detailsChanged} required autoComplete="off" placeholder="Enter the id of the photo here" className={"bg-white text-dark"} disabled />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridPhotoAccessRights"><Form.Label>Photo Access Rights</Form.Label>

                                    <Form.Check type="checkbox" name="ph_access" value={ph_access} onChange={this.detailsChanged} required autoComplete="off" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"right" }}>
                            <Button size="md" type="reset" variant="info" onClick={this.clearAllFields}>
                                <FontAwesomeIcon icon={faUndo}/> Clear
                            </Button>{' '}
                            <Button size="md" type="submit" variant="primary" disabled={this.state.sendingEmail.length === 0 || this.state.recipientEmail.length === 0 || this.state.photoId.length === 0} onClick={this.sharePhoto}>
                                <FontAwesomeIcon icon={faSave}/> Share Photo
                            </Button>{' '}
                            <Button size="md" type="button" variant="info" onClick={this.photoGallery.bind()}>
                                <FontAwesomeIcon icon={faBackward}/> Photo Gallery
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
};

const mapStateToProps = state => {
    return {
        photoObj: state.photo
    }
};

const mapDispatchToProps = dispatch => {
    return {
        getPhoto: (photoId) => dispatch(getPhoto(photoId))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PhotoPadSharePhoto);