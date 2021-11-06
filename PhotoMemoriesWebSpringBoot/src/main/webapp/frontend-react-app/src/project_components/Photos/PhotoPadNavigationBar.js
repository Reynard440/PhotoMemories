import React, {Component} from 'react';
import {Container, Nav, Navbar} from "react-bootstrap";
import logo from '../../logo.png';
import {Link} from 'react-router-dom';

export default class PhotoPadNavigationBar extends Component {
    render() {
        return (
            <Navbar bg="primary" variant="dark">
                <Link to={""} className="navbar-brand">
                    <img src={logo} height="30" width="100" style={{"margin-left":"10px", "margin-bottom":"5px"}} alt={logo}/>
                </Link>
                <Container>
                    <Nav className="mr-auto">
                        <Link to={""} className="navbar-brand">Home</Link>
                        <Link to={"add"} className="navbar-brand">Add Photo</Link>
                        <Link to={"list"} className="navbar-brand">List Photo</Link>
                    </Nav>
                    <Nav className="navbar-right">
                        <Link to={"register"} className="navbar-brand">Register</Link>
                        <Link to={"login"} className="navbar-brand">Login</Link>
                    </Nav>
                </Container>
            </Navbar>
        );
    }
}