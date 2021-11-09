import React, {useState} from "react";
import {Button} from "react-bootstrap";
import {faEdit} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const PhotoPadImage = () => {
    const [isHovered, setHover] = useState(false);

    const imageDisplay = {
        width: "300px",
        height: "300px",
        position: "relative",

    };

    return (
        <div
            style={imageDisplay}
            onMouseOver={() => setHover(true)}
            onMouseLeave={() => setHover(false)}
        >
            <img src={`http://localhost:8095/photo-memories/mvc/v1/c4/displayPhoto/reynardengels@gmail.com/ReynardEngels.jpeg/`} height={"100%"} width={"100%"}/>
            {isHovered && (
                <Button size="sm" style={{position: "absolute", top: "5px", right: "5px", width: "30px"}} variant="primary">
                    <FontAwesomeIcon icon={faEdit}/>
                </Button>
            )}
        </div>
    );
};

export default PhotoPadImage;