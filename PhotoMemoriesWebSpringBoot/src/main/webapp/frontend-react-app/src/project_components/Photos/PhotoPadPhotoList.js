import React, {Component} from "react";
import {Button, ButtonGroup, Card, Table} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faDownload, faEdit, faList, faSave, faShareSquare, faTrash} from '@fortawesome/free-solid-svg-icons';
import PhotoPadToast from "./PhotoPadToast";
import {Link} from "react-router-dom";
import {connect} from 'react-redux';
import {deletePhoto, getPhotos} from "../services/index";
import {Alert} from "bootstrap";

class PhotoPadPhotoList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            photos: []
        };
    }

    componentDidMount() {
        this.props.getPhotos();
    }

    deletePhoto = (photoId, photoLink) => {
        this.props.deletePhoto(photoLink, photoId);
        setTimeout(() => {
            if (this.props.deletedPhotoObj != null) {
                this.setState({"show": true});
                setTimeout(() => this.setState({"show": false}), 1500);
                this.props.getPhotos();
            }else {
                this.setState({"show": false});
            }
        }, 1500);
    };

    downloadPhoto = (photoLink) => {
        window.open("http://localhost:8095/v1/c4/downloadPhoto/reynardengels@gmail.com/"+photoLink);
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

    render(){
        const photoData = this.props.photoData;
        const photos = photoData.photos;
        return (
            <div className={"listMain"}>
                <div style={{"display": this.state.show ? "block": "none"}}>
                    <PhotoPadToast show={this.state.show} message={"Photo deleted, this photo is no longer on the site for you."} type={"danger"}/>
                </div>
                {photoData.error ?
                    <Alert variant="danger">
                        {photoData.error}
                    </Alert> :  <Card className={"border border-dark bg-white text-dark"}>
                        <CardHeader className={"bg-white text-dark"}><FontAwesomeIcon icon={faList}/> Photo List {' '}
                            <Link to={"add"} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faSave}/> Add Photo</Link>
                        </CardHeader>
                        <Card.Body>
                            <Table bordered hover striped variant={"bg-white"}>
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>DATE MODIFIED</th>
                                    <th>CAPTURED BY</th>
                                    <th>FORMAT</th>
                                    <th>NAME</th>
                                    <th>LOCATION</th>
                                    <th>SIZE</th>
                                    <th>DATE UPLOADED</th>
                                    <td>OPERATIONS</td>
                                </tr>
                                </thead>
                                <tbody>
                                {this.state.photos.length === 0 && this.state.photos.confirmation === true ?
                                    <tr align="center">
                                        <td colSpan="7">No Photos Available</td>
                                    </tr> :
                                    photos.map((photo) => (
                                        <tr key={photo.photoId}>
                                            <td>{photo.photoId}</td>
                                            <td>{photo.dateModified}</td>
                                            <td>{photo.photoCapturedBy}</td>
                                            <td>{photo.photoFormat}</td>
                                            <td>{photo.photoName}</td>
                                            <td>{photo.photoLocation}</td>
                                            <td>{(photo.photoSize / 1000000).toFixed(2)} MB</td>
                                            <td>{photo.uploadDate}</td>
                                            <td>
                                                <ButtonGroup>
                                                    <Link to={"edit/"+photo.photoId} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit}/></Link>|
                                                    <Link to={"share/"+photo.photoId} className="btn btn-sm btn-outline-info"><FontAwesomeIcon icon={faShareSquare}/></Link>|
                                                    <Button size="sm" variant="outline-success" onClick={this.downloadPhoto.bind(this, photo.photoLink)}><FontAwesomeIcon icon={faDownload}/></Button>|
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
                }
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        photoData: state.photos,
        deletedPhotoObj: state.photo
    }
};

const mapDispatchToProps = dispatch => {
    return {
        getPhotos: () => dispatch(getPhotos()),
        deletePhoto: (photoId, photoLink) => dispatch(deletePhoto(photoId, photoLink))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PhotoPadPhotoList);