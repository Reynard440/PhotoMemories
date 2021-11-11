import React from 'react';
import {Alert} from "react-bootstrap";

export default function PhotoPadWelcome(props) {
    return (
        <Alert variant="primary">
            <Alert.Heading>{props.title}</Alert.Heading>
            <blockquote className="blockquote mb-0">
                <p>
                    "{props.msg}"
                </p>
                <hr />
                <footer className="blockquote-footer">
                    {props.footer}
                </footer>
            </blockquote>
        </Alert>
    );
}