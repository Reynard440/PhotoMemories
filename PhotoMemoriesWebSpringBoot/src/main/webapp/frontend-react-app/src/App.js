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
import PhotoPadUpdateUser from "./project_components/Users/PhotoPadUpdateUser";
import PhotoPadDeleteUser from "./project_components/Users/PhotoPadDeleteUser";

export default function App() {
    const marginTop = {
        marginTop: "5px",
        marginLeft: "10px"
    };

  return (
    <Router>
        <PhotoPadNavigationBar/>>
            <Container>
                <Row>
                    <Col md={12} style={marginTop}>
                        <Switch>
                            <Route path="/" exact component={PhotoPadWelcome}/>
                            <Route path="/add" exact component={PhotoPadPhoto}/>
                            <Route path="/edit/:photoId" exact component={PhotoPadEdit}/>
                            <Route path="/list" exact component={PhotoPadPhotoList}/>
                            <Route path="/share" exact component={PhotoPadSharePhoto}/>
                            <Route path="/gallery" exact component={PhotoPadGallery}/>
                            <Route path="/register" exact component={PhotoPadRegister}/>
                            <Route path="/login" exact component={PhotoPadLogin}/>
                            <Route path="/update" exact component={PhotoPadUpdateUser}/>
                            <Route path="/delete" exact component={PhotoPadDeleteUser}/>
                        </Switch>
                    </Col>
                </Row>
            </Container>
        <PhotoPadFooter/>
    </Router>
  );
};
