import React, {Component} from 'react';
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBackward, faSave, faShareSquare, faUndo} from "@fortawesome/free-solid-svg-icons";
import {Button, Card, Col, Form, Row} from "react-bootstrap";

export default class PhotoPadSharePhoto extends Component {

    photoList = () => {
        return this.props.history.push("/list");
    };

    render() {
        return (
            <div>
                <Card className={"border border-white bg-white text-dark"}>
                    <CardHeader><FontAwesomeIcon icon={faShareSquare}/> Share a Photo with Someone </CardHeader>
                    <Form id={"photoForm"}>
                        <Card.Body>
                            <Row>
                                <Form.Group as={Col} controlId="formGridToEmail">
                                    <Form.Label>Send to</Form.Label>
                                    <Form.Control type="email" name="receivingEmail"  onChange={this.photoChanged} required autoComplete="off" placeholder="Enter modified date" className={"bg-white text-dark"} />
                                </Form.Group>

                                <Form.Group as={Col} controlId="formGridPhotoId">
                                    <Form.Label>Photo Id</Form.Label>
                                    <Form.Control type="text" name="location" onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of city" className={"bg-white text-dark"} />
                                </Form.Group>
                            </Row>
                            <Row>
                                <Form.Group as={Col} controlId="formGridPhotoAccessRights">
                                    <Form.Label>Photo Access Rights</Form.Label>
                                    <Form.Check type="checkbox" name="ph_name" onChange={this.photoChanged} required autoComplete="off" placeholder="Enter name of photo date" className={"bg-white text-dark"} />
                                    <Form.Label>Full Access = checked</Form.Label>
                                    <br/>
                                    <Form.Label>Read Access = unchecked</Form.Label>
                                </Form.Group>
                            </Row>
                        </Card.Body>
                        <Card.Footer style={{ "textAlign":"right" }}>
                            <Button size="md" type="reset" variant="info">
                                <FontAwesomeIcon icon={faUndo}/> Clear
                            </Button>{' '}
                            <Button size="md" type="submit" variant="primary" >
                                <FontAwesomeIcon icon={faSave}/> Share Photo
                            </Button>{' '}
                            <Button size="md" type="button" variant="info" onClick={this.photoList.bind()}>
                                <FontAwesomeIcon icon={faBackward}/> Photo List
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
};