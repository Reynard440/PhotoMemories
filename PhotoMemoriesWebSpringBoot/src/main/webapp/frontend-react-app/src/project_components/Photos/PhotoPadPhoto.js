import React, {Component} from "react";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faImages, faPlusSquare, faSave, faUndo} from '@fortawesome/free-solid-svg-icons';
import PhotoPadToast from "./PhotoPadToast";
import {connect} from "react-redux";
import {savePhoto} from "../services/index";

class PhotoPadPhoto extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state.show = false;
        this.state.unsupportedFileType = false;
        this.state.isFourHundred = false;
        this.photoChanged = this.photoChanged.bind(this);
        this.addPhoto = this.addPhoto.bind(this);
    }

    initialState = {
        modifiedDate:'', ph_name:'', location:'', ph_captured:'', photo:''
    };

    clearAllFields = () => {
        this.setState(() => this.initialState);
    };

    photoCollection = () => {
        return this.props.history.push("/gallery");
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
                setTimeout(() => this.setState({"show": false}), 3000);
                this.setState(this.initialState);
                setTimeout(() => this.photoCollection(), 3000);
            } else {
                this.setState({"show": false});
                if (this.props.savedPhotoObj.error.response.status === 403) {
                    this.setState({"isFourHundred":false});
                    this.setState({"unsupportedFileType" : true, "method":"post"});
                    setTimeout(() => this.setState({"unsupportedFileType": false}), 4000);
                } else if (this.props.savedPhotoObj.error.response.status === 400) {
                    this.setState({"unsupportedFileType":false});
                    this.setState({"isFourHundred" : true, "method":"post"});
                    setTimeout(() => this.setState({"isFourHundred": false}), 4000);
                }
            }
        }, 1500);
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
                <div style={{"display": this.state.unsupportedFileType ? "block": "none"}}>
                    <PhotoPadToast show={this.state.unsupportedFileType} message={"Unsupported format, please upload the content in a different format."} type={"danger"}/>
                </div>
                <div style={{"display": this.state.isFourHundred ? "block": "none"}}>
                    <PhotoPadToast show={this.state.isFourHundred} message={"Ensure that the provided information is correct."} type={"400"}/>
                </div>
                <Card className={"border border-white bg-white text-dark"}>
                    <CardHeader><FontAwesomeIcon icon={faPlusSquare}/> {this.state.photoId ? "Update a Photo":"Add a Photo"}</CardHeader>
                    <Form onReset={this.clearAllFields} onSubmit={this.addPhoto} id={"photoForm"}>
                        <Card.Body>
                            <Row>
                                <Form.Group as={Col} controlId="formGridPhoto">
                                    <Form.Label><strong>First upload the photo</strong></Form.Label>
                                    <Form.Control type="file" name="photo" values={photo} onChange={this.photoChanged} required autoComplete="off" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
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
                                    <Form.Control type="text" name="ph_captured" value={ph_captured} onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of person" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"right" }}>
                            <Button size="md" type="reset" variant="info" onClick={this.clearAllFields}>
                                <FontAwesomeIcon icon={faUndo}/> Clear
                            </Button>{' '}
                            <Button size="md" type="button" variant="primary" onClick={this.photoCollection.bind()}>
                                <FontAwesomeIcon icon={faImages}/> Photo Collection
                            </Button>{' '}
                            <Button size="md" type="submit" variant="success" disabled={this.state.modifiedDate.value === 'yyyy/mm/dd' || this.state.ph_name.length === 0 || this.state.ph_captured.length === 0 || this.state.location.length === 0}  onClick={this.addPhoto}>
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