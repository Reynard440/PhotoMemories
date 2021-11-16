import React, {Component} from 'react';
import CardHeader from "react-bootstrap/CardHeader";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faDownload, faEdit, faImages, faSave, faShareSquare, faTrash} from "@fortawesome/free-solid-svg-icons";
import {Button, Card} from "react-bootstrap";
import {Link} from "react-router-dom";
import {deletePhoto, getPhotos} from "../services";
import {connect} from "react-redux";
import PhotoPadToast from "./PhotoPadToast";

class PhotoPadGallery extends Component {
    constructor(props) {
        super(props);
        this.state = {
            photos: [],
            deleteGalleryError: false
        };
    }

    componentDidMount() {
        this.props.getPhotos();
    }

    deletePhoto = (photoId, photoLink) => {
        this.props.deletePhoto(photoLink, photoId);
        setTimeout(() => {
            if (this.props.deletedPhotoObj.error === '') {
                this.setState({"show": true});
                setTimeout(() => this.setState({"show": false}), 3000);
                this.props.getPhotos();
            }else {
                this.setState({"show": false});
                if (this.props.deletedPhotoObj.error !== '') {
                    this.setState({"deleteGalleryError" : true, "method":"post"});
                    setTimeout(() => this.setState({"deleteGalleryError": false}), 4000);
                }
            }
        }, 1500);
    };

    downloadPhoto = (photoLink) => {
        window.open("http://localhost:8095/v1/c4/downloadPhoto/" + localStorage.userEmail + "/"+photoLink);
    };

    render() {
        const photoData = this.props.photoData;
        const photos = photoData.photos;
        return (
            <div>
                <div style={{"display": this.state.deleteGalleryError ? "block": "none"}}>
                    <PhotoPadToast show={this.state.deleteGalleryError} message={"Access Required, please consult with the owner."} type={"ad"}/>
                </div>
                <div className={"galleryMain"}>
                    <Card className={"border border-dark bg-white text-dark galleryCard"}>
                        <CardHeader className={"bg-white text-dark"} style={{textAlign: 'left'}}><FontAwesomeIcon icon={faImages}/> Your Gallery of Photos {'  '}
                            <Link to={"add"} className="btn btn-sm btn-outline-primary float-end" ><FontAwesomeIcon icon={faSave}/> Add Photo</Link>
                        </CardHeader>
                        {photoData.photos.length === 0 || this.state.photos.confirmation === true ?
                            <Card.Body>
                                No Current Photos To Display for: {localStorage.userEmail}
                            </Card.Body> :
                            <Card.Body>
                                <div>
                                    {photos.map((photo) => (
                                        <div key={photo.photoId} className={"grouping"}>
                                            <Card>
                                                <CardHeader className="bg-light">
                                                    <div className={"divText"}><strong>ID:</strong> {photo.photoId}</div>
                                                </CardHeader>
                                                <Card.Img src={`http://localhost:8095/v1/c4/displayPhoto/` + localStorage.userEmail + `/` + photo.photoLink + `/`}
                                                          className={"containerImage"} alt={"default"}/>
                                                <Card.Body>
                                                    <div className={"divText"}><strong>Name:</strong> {photo.photoName}</div>
                                                    <div className={"divText"}><strong>Uploaded:</strong> {photo.uploadDate}</div>
                                                    <div className={"divText"}><strong>Captured By:</strong> {photo.photoCapturedBy}</div>
                                                    <div className={"divText"}><strong>Format:</strong> {photo.photoFormat}</div>
                                                    <div className={"divText"}><strong>Location:</strong> {photo.photoLocation}</div>
                                                    <div className={"divText"}><strong>Size:</strong> {(photo.photoSize / 1000000).toFixed(2)} MB</div>
                                                </Card.Body>
                                                <Card.Footer className="bg-light">
                                                    <Link to={"edit/" + photo.photoId}
                                                          className="btn btn-sm btn-outline-primary"><FontAwesomeIcon
                                                        icon={faEdit}/></Link>|
                                                    <Link to={"share/" + photo.photoId}
                                                          className="btn btn-sm btn-outline-info"><FontAwesomeIcon
                                                        icon={faShareSquare}/></Link>|
                                                    <Button size="sm" variant="outline-success"
                                                            onClick={this.downloadPhoto.bind(this, photo.photoLink)}><FontAwesomeIcon
                                                        icon={faDownload}/></Button>|
                                                    <Button size="sm" variant="outline-danger"
                                                            onClick={this.deletePhoto.bind(this, photo.photoLink, photo.photoId)}><FontAwesomeIcon
                                                        icon={faTrash}/></Button>
                                                </Card.Footer>
                                            </Card>
                                        </div>
                                    ))}
                                </div>
                            </Card.Body>
                        }
                    </Card>
                </div>
            </div>
        );
    }
};

const mapStateToProps = state => {
    return {
        photoData: state.photos,
        deletedPhotoObj: state.photo
    }
};

const mapDispatchToProps = dispatch => {
    return {
        getPhotos: (email) => dispatch(getPhotos(email)),
        deletePhoto: (photoId, photoLink) => dispatch(deletePhoto(photoId, photoLink))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PhotoPadGallery);