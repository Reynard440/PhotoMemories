import React, {Component} from "react";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faImages, faList, faPlusSquare, faSave, faUndo} from '@fortawesome/free-solid-svg-icons';
import PhotoPadToast from "./PhotoPadToast";
import {connect} from "react-redux";
import {savePhoto} from "../services/index";

class PhotoPadPhoto extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state.show = false;
        this.photoChanged = this.photoChanged.bind(this);
        this.addPhoto = this.addPhoto.bind(this);
    }

    initialState = {
        modifiedDate:'', ph_name:'', location:'', ph_captured:'', photo:''
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
        bodyInfo.append("email", localStorage.userEmail);
        bodyInfo.append("photo", this.state.photo);

        this.props.savePhoto(bodyInfo);
        setTimeout(() => {
            if (this.props.savedPhotoObj.photo != null) {
                this.setState({"show": true, "method":"post"});
                setTimeout(() => this.setState({"show": false}), 1000);
                setTimeout(() => this.photoList(), 1000);
            } else {
                this.setState({"show": false});
            }
        }, 1000);
        this.setState(this.initialState);
    };

    photoChanged = (event) => {
        if (event.target.name === "photo") {
            // eslint-disable-next-line
            this.state.photo = event.target.files[0];
        } else {
            this.setState({
                [event.target.name] : event.target.value
            });
        }
    };

    photoGallery = () => {
        return this.props.history.push("/gallery");
    };

    render(){
        const {modifiedDate, ph_name, location, ph_captured, photo} = this.state;
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
                                <Form.Group as={Col} controlId="formGridPhoto">
                                        <Form.Label>Photo</Form.Label>
                                        <Form.Control type="file" name="photo" value={photo} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter your email" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"right" }}>
                            <Button size="md" type="reset" variant="info" onClick={this.clearAllFields}>
                                <FontAwesomeIcon icon={faUndo}/> Clear
                            </Button>{' '}
                            <Button size="md" type="button" variant="primary" onClick={this.photoList.bind()}>
                                <FontAwesomeIcon icon={faList}/> Photo List
                            </Button>{' '}
                            <Button size="md" type="button" variant="primary" onClick={this.photoGallery.bind()}>
                                <FontAwesomeIcon icon={faImages}/> Photo Gallery
                            </Button>{' '}
                            <Button size="md" type="submit" variant="success" disabled={this.state.ph_name.length === 0 || this.state.ph_captured.length === 0 || this.state.location.length === 0}  onClick={this.addPhoto}>
                                <FontAwesomeIcon icon={faSave}/> Add Photo
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
        savedPhotoObj: state.photo
    }
};

const mapDispatchToProps = dispatch => {
    return {
        savePhoto: (photo) => dispatch(savePhoto(photo))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PhotoPadPhoto);