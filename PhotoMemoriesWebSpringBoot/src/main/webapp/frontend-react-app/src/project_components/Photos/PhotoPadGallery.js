import React, {Component} from 'react';
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faDownload, faEdit, faImages, faSave, faShareSquare, faTrash} from "@fortawesome/free-solid-svg-icons";
import {Button, Card} from "react-bootstrap";
import axios from 'axios';
import {Link} from "react-router-dom";

export default class PhotoPadGallery extends Component {
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
        window.open("http://localhost:8095/photo-memories/mvc/v1/c4/downloadPhoto/reynardengels@gmail.com/"+photoLink);
        // axios.get("http://localhost:8095/photo-memories/mvc/v1/c4/downloadPhoto/reynardengels@gmail.com/"+photoLink)
        //     .then(res => {
        //         if (res.data !== null) {
        //             this.setState({"show": true});
        //             setTimeout(() => this.setState({"show": false}), 3000);
        //         }else {
        //             this.setState({"show": false});
        //         }
        //     });
    };

    render() {
        return (
            <div className={"galleryMain"}>
                <Card className={"border border-dark bg-white text-dark galleryCard"}>
                    <CardHeader className={"bg-white text-dark"} style={{textAlign: 'left'}}><FontAwesomeIcon icon={faImages}/> Your Gallery of Photos {'  '}
                        <Link to={"add"} className="btn btn-sm btn-info" ><FontAwesomeIcon icon={faSave}/> Add Photo</Link>
                    </CardHeader>
                    <Card.Body>
                        <div>
                            {this.state.photos.map((photo) => (
                                <div key={photo.photoId} className={"grouping"}>
                                    <img src={`http://localhost:8095/photo-memories/mvc/v1/c4/displayPhoto/reynardengels@gmail.com/` + photo.photoLink + `/`} className={"containerImage"} alt={"default"}/>
                                    <div className={"divText"}>{photo.photoId}</div>
                                    <Link to={"edit/"+photo.photoId} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit}/></Link>|
                                    <Link to={"share/"+photo.photoId} className="btn btn-sm btn-outline-info"><FontAwesomeIcon icon={faShareSquare}/></Link>|
                                    <Button size="sm" variant="outline-success" onClick={this.downloadPhoto.bind(this, photo.photoLink)} ><FontAwesomeIcon icon={faDownload}/></Button>|
                                    <Button size="sm" variant="outline-danger" onClick={this.deletePhoto.bind(this, photo.photoLink , photo.photoId)}><FontAwesomeIcon icon={faTrash}/></Button>
                                </div>
                            ))}
                        </div>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}