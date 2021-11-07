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

export default function App() {
    const marginTop = {
        marginTop: "20px",
        marginLeft: "10px"
    };

  return (
    <Router>
        <PhotoPadNavigationBar/>
        <Container>
            <Row>
                <Col lg={12} style={marginTop}>
                    <Switch>
                        <Route path="/" exact component={PhotoPadWelcome}/>
                        <Route path="/add" exact component={PhotoPadPhoto}/>
                        <Route path="/edit/:photoId" exact component={PhotoPadPhoto}/>
                        <Route path="/list" exact component={PhotoPadPhotoList}/>
                        <Route path="/register" exact component={PhotoPadRegister}/>
                        <Route path="/login" exact component={PhotoPadLogin}/>
                    </Switch>
                </Col>
            </Row>
        </Container>
        <PhotoPadFooter/>
    </Router>
  );
}
