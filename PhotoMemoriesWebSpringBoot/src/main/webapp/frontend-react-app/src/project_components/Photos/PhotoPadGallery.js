import React, {Component} from 'react';
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faImages} from "@fortawesome/free-solid-svg-icons";
import {Card} from "react-bootstrap";
import axios from 'axios';

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
                console.log(data);
                this.setState({photos: data.cargo});
            });
        console.log(this.state.photos);
    };

    render() {
        return (
            <div>
                <Card className={"border border-dark bg-white text-dark"} style={{ width: '80em' }}>
                    <CardHeader className={"bg-white text-dark"}><FontAwesomeIcon icon={faImages}/> Your Gallery of Photos</CardHeader>
                    <Card.Body>
                        <div>
                            {this.state.photos.map((photo) => (
                                <div className={"grouping"}>
                                    <img key={photo.photoId} src={`http://localhost:8095/photo-memories/mvc/v1/c4/displayPhoto/reynardengels@gmail.com/` + photo.photoLink + `/`} className={"containerImage"} height={"300px"} width={"300px"}  alt={"default"}/>
                                    <div className={"divText"}>{photo.photoId}</div>
                                </div>
                            ))}
                        </div>
                    </Card.Body>
                    <Card.Body>
                        <Card.Link href="#">Card Link</Card.Link>
                    </Card.Body>
                </Card>
            </div>
        );
    }
}