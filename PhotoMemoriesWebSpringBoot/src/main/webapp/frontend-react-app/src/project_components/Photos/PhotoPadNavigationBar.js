import React, {Component} from 'react';
import {Container, Nav, Navbar} from "react-bootstrap";
import logo from '../../logo.png';
import {Link} from 'react-router-dom';
import {
    faHome,
    faImages,
    faKey,
    faList,
    faSave,
    faSignInAlt,
    faTrash,
    faUserEdit
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class PhotoPadNavigationBar extends Component {
    render() {
        return (
            <Navbar bg="primary" variant="dark" expand="lg">
                <Link to={""} className="navbar-brand">
                    <img src={logo} height="30" width="100" style={{"marginLeft":"10px", "marginBottom":"5px"}} alt={logo}/>
                </Link>
                <Container fluid>
                    <Nav className="mr-auto">
                        <Link to={""} className="navbar-brand"><FontAwesomeIcon icon={faHome}/> Home</Link>
                        <Link to={"add"} className="navbar-brand"><FontAwesomeIcon icon={faSave}/> Add Photo</Link>
                        <Link to={"list"} className="navbar-brand"><FontAwesomeIcon icon={faList}/> List Photo</Link>
                        <Link to={"gallery"} className="navbar-brand"><FontAwesomeIcon icon={faImages}/> Gallery</Link>
                    </Nav>
                    <Nav className="navbar-right">
                        <Link to={"register"} className="navbar-brand"><FontAwesomeIcon icon={faKey}/> Register</Link>
                        <Link to={"login"} className="navbar-brand"><FontAwesomeIcon icon={faSignInAlt}/> Login</Link>
                        <Link to={"update"} className="navbar-brand"><FontAwesomeIcon icon={faUserEdit}/> Update</Link>
                        <Link to={"delete"} className="navbar-brand"><FontAwesomeIcon icon={faTrash}/> Delete</Link>
                    </Nav>
                </Container>
            </Navbar>
        );
    }
}