import React, {Component} from 'react';
import {Col, Container, Navbar} from "react-bootstrap";
import {faAt} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class PhotoPadFooter extends Component {
    render() {
        let Year = new Date().getFullYear();
        return (
            <Navbar fixed="bottom" bg="primary" variant="light">
                <Container>
                    <Col lg={12} className="text-center text-white">
                        <div>
                            <FontAwesomeIcon icon={faAt}/>All Rights Reserved by PhotoPad, {Year} - {Year+1}
                        </div>
                    </Col>
                </Container>
            </Navbar>
        );
    }
}