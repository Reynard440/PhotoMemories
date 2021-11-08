import React, {Component} from "react";
import {Button, ButtonGroup, Card, Table} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faDownload, faEdit, faList, faShareSquare, faTrash} from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import PhotoPadToast from "./PhotoPadToast";
import {Link} from "react-router-dom";

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
    };

    deletePhoto = (photoLink, photoId) => {
        axios.delete("http://localhost:8095/photo-memories/mvc/v1/c2/deletePhoto/"+photoLink+"/reynardengels@gmail.com/"+photoId)
            .then(res => {
                if (res.data != null) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show": false}), 3000);
                    this.setState({
                        photos: this.state.photos.filter(photo => photo.photoId !== photoId)
                    });
                }else {
                    this.setState({"show": false});
                }
            });
    };

    downloadPhoto = (photoLink) => {
      axios.get("http://localhost:8095/photo-memories/mvc/v1/c4/downloadPhoto/reynardengels@gmail.com/"+photoLink)
          .then(res => {
              if (res.data !== null) {
                  this.setState({"show": true});
                  setTimeout(() => this.setState({"show": false}), 3000);
              }else {
                  this.setState({"show": false});
              }
          });
    };

    render(){
        return (
            <div>
                <div style={{"display": this.state.show ? "block": "none"}}>
                    <PhotoPadToast show={this.state.show} message={"Photo deleted, this photo is no longer on the site for you."} type={"info"}/>
                </div>
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
                                                <Link to={"edit/"+photo.photoId} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit}/></Link> |
                                                <Button size="sm" variant="outline-info"><FontAwesomeIcon icon={faShareSquare}/></Button> |
                                                <Button size="sm" variant="outline-success" onClick={this.downloadPhoto.bind(this, photo.photoLink)}><FontAwesomeIcon icon={faDownload}/></Button> |
                                                <Button size="sm" variant="outline-danger" onClick={this.deletePhoto.bind(this, photo.photoLink , photo.photoId)} ><FontAwesomeIcon icon={faTrash}/></Button>
                                            </ButtonGroup>
                                        </td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </Table>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}
