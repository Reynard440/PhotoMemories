import React, {Component} from "react";
import {Button, ButtonGroup, Card, Table} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faEdit, faList, faTrash} from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';

export default class PhotoPadPhotoList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            photos: []
        };
    }

    componentDidMount() {
        this.loadPhotos();
    }

    loadPhotos() {
        axios.get("http://localhost:8095/photo-memories/mvc/v1/c2/loadAllPhotosOfUser/reynardengels@gmail.com/")
            .then(res => res.data)
            .then((data) => {
                this.setState({photos: data.cargo});
            });
    }

    render(){
        return (
            <Card className={"border border-dark bg-white text-dark"}>
                <CardHeader className={"bg-white text-dark"}><FontAwesomeIcon icon={faList}/> Photo List</CardHeader>
                <Card.Body>
                    <Table bordered hover striped variant={"bg-white"}>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Date Modified</th>
                                <th>Captured By</th>
                                <th>Format</th>
                                <th>Link</th>
                                <th>Location</th>
                                <th>Date Uploaded</th>
                                <td>Operations</td>
                            </tr>
                        </thead>
                        <tbody>
                        {this.state.photos.length === 0 && this.state.photos.confirmation === true ?
                            <tr align="center">
                                <td colSpan="7">No Photos Available</td>
                            </tr> :
                            this.state.photos.map((photo) => (
                                <tr key={photo.photoId}>
                                    <td>{photo.photoId}</td>
                                    <td>{photo.dateModified}</td>
                                    <td>{photo.photoCapturedBy}</td>
                                    <td>{photo.photoFormat}</td>
                                    <td>{photo.photoLink}</td>
                                    <td>{photo.photoLocation}</td>
                                    <td>{photo.uploadDate}</td>
                                    <td>
                                        <ButtonGroup>
                                            <Button size="sm" variant="outline-success"><FontAwesomeIcon icon={faEdit}/></Button> {' '}
                                            <Button size="sm" variant="outline-danger"><FontAwesomeIcon icon={faTrash}/></Button>
                                        </ButtonGroup>
                                    </td>
                                </tr>
                            ))
                            // <tr>
                            //     <th>1</th>
                            //     <th>2012-02-10</th>
                            //     <th>Reyno Engels</th>
                            //     <th>jpeg</th>
                            //     <th>Vaalpark</th>
                            //     <th>ReynardEngels.jpeg</th>
                            //     <th>1113487</th>
                            //     <th>2021-10-28</th>
                            // </tr>
                        }
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        );
    }
}
