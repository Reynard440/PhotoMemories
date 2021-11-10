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
        // const [photos, setPhotos] = useState();
        return (
            <div>
                <Card className={"border border-dark bg-white text-dark"} style={{ width: '100%' }}>
                    <CardHeader className={"bg-white text-dark"}><FontAwesomeIcon icon={faImages}/> Your Gallery of Photos</CardHeader>
                    <Card.Body>
                        {this.state.photos.map((photo) => (
                            <img key={photo.photoId} src={`http://localhost:8095/photo-memories/mvc/v1/c4/displayPhoto/reynardengels@gmail.com/`+ photo.photoLink + `/`} height={"300px"} width={"300px"} alt={"default"}/>
                        ))}
                        {/*<PhotoPadImage/>*/}
                    </Card.Body>
                </Card>
            </div>
        );
    }
}