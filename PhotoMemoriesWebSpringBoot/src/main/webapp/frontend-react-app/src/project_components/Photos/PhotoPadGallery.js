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
        console.log(this.state.photos);
    }

    loadPhotos() {
        axios.get("http://localhost:8095/photo-memories/mvc/v1/c2/getAllPhotosForUser/reynardengels@gmail.com/")
            .then(res => res.data)
            .then((data) => {
                this.setState({photos: data});
            });
        const photos = this.state.photos.map((photo) => {
            return <img src={photo.url} />;
        });
        return <div>{photos}</div>;
    };

    render() {
        return (
            <div>
                <Card className={"border border-dark bg-white text-dark"}>
                    <CardHeader className={"bg-white text-dark"}><FontAwesomeIcon icon={faImages}/> Your Gallery of Photos</CardHeader>
                    <Card.Body>
                        <div>
                            <img src={`http://localhost:8095/photo-memories/mvc/v1/c2/getAllPhotosForUser/reynardengels@gmail.com/`} />
                        </div>
                        {/*<Table bordered hover striped variant={"bg-white"}>*/}
                        {/*    <thead>*/}
                        {/*    <tr>*/}
                        {/*        <th>#</th>*/}
                        {/*        <th>Date Modified</th>*/}
                        {/*        <th>Captured By</th>*/}
                        {/*        <th>Format</th>*/}
                        {/*        <th>Link</th>*/}
                        {/*        <th>Location</th>*/}
                        {/*        <th>Date Uploaded</th>*/}
                        {/*        <td>Operations</td>*/}
                        {/*    </tr>*/}
                        {/*    </thead>*/}
                        {/*    <tbody>*/}
                        {/*    {this.state.photos.length === 0 && this.state.photos.confirmation === true ?*/}
                        {/*        <tr align="center">*/}
                        {/*            <td colSpan="7">No Photos Available</td>*/}
                        {/*        </tr> :*/}
                        {/*        this.state.photos.map((photo) => (*/}
                        {/*            <tr key={photo.photoId}>*/}
                        {/*                <td>{photo.photoId}</td>*/}
                        {/*                <td>{photo.dateModified}</td>*/}
                        {/*                <td>{photo.photoCapturedBy}</td>*/}
                        {/*                <td>{photo.photoFormat}</td>*/}
                        {/*                <td>{photo.photoLink}</td>*/}
                        {/*                <td>{photo.photoLocation}</td>*/}
                        {/*                <td>{photo.uploadDate}</td>*/}
                        {/*                <td>*/}
                        {/*                    <ButtonGroup>*/}
                        {/*                        <Link to={"edit/"+photo.photoId} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit}/></Link> |*/}
                        {/*                        <Button size="sm" variant="outline-info"><FontAwesomeIcon icon={faShareSquare}/></Button> |*/}
                        {/*                        <Button size="sm" variant="outline-success" onClick={this.downloadPhoto.bind(this, photo.photoLink)}><FontAwesomeIcon icon={faDownload}/></Button> |*/}
                        {/*                        <Button size="sm" variant="outline-danger" onClick={this.deletePhoto.bind(this, photo.photoLink , photo.photoId)} ><FontAwesomeIcon icon={faTrash}/></Button>*/}
                        {/*                    </ButtonGroup>*/}
                        {/*                </td>*/}
                        {/*            </tr>*/}
                        {/*        ))*/}
                        {/*    }*/}
                        {/*    </tbody>*/}
                        {/*</Table>*/}
                    </Card.Body>
                </Card>
            </div>
        );
    }
}