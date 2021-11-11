import React from 'react';
import {Container, Nav, Navbar} from "react-bootstrap";
import logo from '../../logo.png';
import {Link} from 'react-router-dom';
import {faHome, faImages, faKey, faList, faSignInAlt} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default function PhotoPadNavigationBar() {
    return (
        <Navbar bg="primary" expanded={"sm"} variant="dark" expand="lg">
            <Link to={""} className="navbar-brand">
                <img src={logo} height="20" width="100" style={{"marginLeft":"10px"}} alt={logo}/>
            </Link>
            <Container fluid>
                <Nav className="mr-auto">
                    <Link to={""} className="navbar-brand"><FontAwesomeIcon icon={faHome}/> Home</Link>
                    <Link to={"list"} className="navbar-brand"><FontAwesomeIcon icon={faList}/> Photo List</Link>
                    <Link to={"gallery"} className="navbar-brand"><FontAwesomeIcon icon={faImages}/> Gallery</Link>
                </Nav>
                <Nav className="navbar-right">
                    <Link to={"register"} className="navbar-brand"><FontAwesomeIcon icon={faKey}/> Register</Link>
                    <Link to={"login"} className="navbar-brand"><FontAwesomeIcon icon={faSignInAlt}/> Login</Link>
                </Nav>
            </Container>
        </Navbar>
    );
}