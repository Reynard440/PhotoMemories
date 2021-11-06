import React, {Component} from 'react';
import {Alert} from "react-bootstrap";

export default class PhotoPadWelcome extends Component {
    render() {
        return (
            <Alert variant="secondary">
                <Alert.Heading>Hi and welcome to my project 2 website</Alert.Heading>
                <blockquote className="blockquote mb-0">
                    <p>
                        "Of all the things I've ever done, this was by far the most exciting, stressful, and rewarding
                        task any of my modules (over the 3 years) has given me to complete. It was a lot of fun, and rewarding
                        but challenging at the same time."
                    </p>
                    <hr />
                    <footer className="blockquote-footer">
                        Reynard Engels
                    </footer>
                </blockquote>
            </Alert>
        );
    };
}