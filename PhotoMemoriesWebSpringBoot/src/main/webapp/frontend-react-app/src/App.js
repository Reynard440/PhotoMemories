import React from 'react';
import './App.css';
import PhotoPadNavigationBar from "./project_components/Photos/PhotoPadNavigationBar";
import {Col, Container, Row} from "react-bootstrap";
import PhotoPadWelcome from "./project_components/Photos/PhotoPadWelcome";
import PhotoPadFooter from "./project_components/Photos/PhotoPadFooter";
import PhotoPadPhoto from "./project_components/Photos/PhotoPadPhoto";
import PhotoPadPhotoList from "./project_components/Photos/PhotoPadPhotoList";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import PhotoPadRegister from './project_components/Users/PhotoPadRegister';
import PhotoPadLogin from './project_components/Users/PhotoPadLogin';
import PhotoPadGallery from "./project_components/Photos/PhotoPadGallery";
import PhotoPadEdit from "./project_components/Photos/PhotoPadEdit";
import PhotoPadSharePhoto from "./project_components/Photos/PhotoPadSharePhoto";

export default function App() {
    window.onbeforeunload = (event) => {
        const ev = event || window.event;
        ev.preventDefault();
        if (ev) {
            ev.returnValue = '';
        }
        return '';
    };

    const marginTop = {
        marginTop: "5px",
        marginLeft: "0"
    };

    const title = "Hi and welcome to my project 2 website";
    const msg = "Of all the things I've ever done, this was by far the most exciting, stressful, and rewarding\n" +
        "                    task any of my modules (over the 3 years) has given me to complete. It was a lot of fun, and rewarding\n" +
        "                    but challenging at the same time.";
    const footer = "Reynard Engels"

  return (
    <Router>
        <PhotoPadNavigationBar/>>
            <Container>
                <Row>
                    <Col md={12} style={marginTop}>
                        <Switch>
                            <Route path="/" exact component={() => <PhotoPadWelcome title={title} msg={msg} footer={footer}/>}/>
                            <Route path="/add" exact component={PhotoPadPhoto}/>
                            <Route path="/edit/:photoId" exact component={PhotoPadEdit}/>
                            <Route path="/list" exact component={PhotoPadPhotoList}/>
                            <Route path="/share/:photoId" exact component={PhotoPadSharePhoto}/>
                            <Route path="/gallery" exact component={PhotoPadGallery}/>
                            <Route path="/register" exact component={PhotoPadRegister}/>
                            <Route path="/login" exact component={PhotoPadLogin}/>
                            <Route path="/logout" exact component={() => <PhotoPadLogin message="Logged out successfully."/>}/>
                        </Switch>
                    </Col>
                </Row>
            </Container>
        <PhotoPadFooter/>
    </Router>
  );
};
