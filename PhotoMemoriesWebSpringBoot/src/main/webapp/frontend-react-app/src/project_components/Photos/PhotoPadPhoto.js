import React, {Component} from "react";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare, faSave} from '@fortawesome/free-solid-svg-icons';

export default  class PhotoPadPhoto extends Component {
    constructor(props) {
        super(props);
        this.state = { mod_date:'', ph_name:'', location:'', ph_captured:'', email:'', photo:'' };
        this.photoChanged = this.photoChanged.bind(this);
        this.addPhoto = this.addPhoto.bind(this);
    }

    addPhoto (event) {
        alert(this.state.mod_date);
        alert(this.state.ph_name);
        alert(this.state.location);
        alert(this.state.ph_captured);
        alert(this.state.email);
        alert(this.state.photo);
        event.preventDefault();
    }

    photoChanged(event) {
        this.setState({
            [event.target.name] : event.target.value
        });
    }

    render(){
        return (
            <Card className={"border border-secondary bg-secondary text-white"}>
                <CardHeader><FontAwesomeIcon icon={faPlusSquare}/> Add a Photo</CardHeader>
                    <Form onSubmit={this.addPhoto} id={"photoForm"}>
                        <Card.Body>
                        <Row>
                            <Form.Group as={Col} controlId="formGridModifiedDate">
                                <Form.Label>Date Modified</Form.Label>
                                <Form.Control type="date" value={this.state.mod_date} onChange={this.photoChanged} required placeholder="Enter modified date" className={"bg-white text-dark"} />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridPhotoName">
                                <Form.Label>Photo Name</Form.Label>
                                <Form.Control type="text" value={this.state.ph_name} onChange={this.photoChanged} required  placeholder="Enter name of photo date" className={"bg-white text-dark"} />
                            </Form.Group>
                        </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridLocation">
                                    <Form.Label>Photo Location</Form.Label>
                                    <Form.Control type="text" value={this.state.location} onChange={this.photoChanged} required  placeholder="Enter name of city" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridCaptured">
                                    <Form.Label>Capture By?</Form.Label>
                                    <Form.Control type="text" value={this.state.ph_captured} onChange={this.photoChanged} required  placeholder="Enter name of person" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridEmail">
                                    <Form.Label>User email</Form.Label>
                                    <Form.Control type="email" value={this.state.email} onChange={this.photoChanged} required  placeholder="Enter your email" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridPhoto">
                                    <Form.Label>Default file input example</Form.Label>
                                    <Form.Control type="file" value={this.state.photo} onChange={this.photoChanged} required  className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"left"}}>
                            <Button size="md" type="Submit" variant="success" type="submit">
                                <FontAwesomeIcon icon={faSave}/> Add Photo
                            </Button>
                        </Card.Footer>
                    </Form>
            </Card>
        );
    }
}