import React from "react";
import {Toast} from "react-bootstrap";

export default function PhotoPadToast(props) {
    const details = {
        position: 'fixed',
        top: '10px',
        left: '10px',
        zIndex: '1',
        boxShadow: '0 4px 8px o rgba(0.5, 0.5, 0.5, 0.2), 0 6px 20px 0 rgba(0.8, 0.8, 0.8, 0.19)'
    };
    return (
        <div style={props.show ? details : null}>
            <Toast className={`border text-white ${props.type === "success" ? "border-success bg-success" : "danger" ? "border-danger bg-danger" : "info" ? "border-info bg-info" : "border-warning bg-warning"}`} show={props.show}>
                <Toast.Header className={`text-white ${props.type === "success" ? "bg-success" : "danger" ? "bg-danger" : "info" ? "bg-info" : "bg-warning"}`} closeButton={false}>
                    {props.type === "success" ?
                        <strong className="mr-auto">Success</strong> : props.type === "danger" ?
                            <strong className="mr-auto">Danger</strong> : props.type === "403" ?
                            <strong className="mr-auto">HTTP-403</strong> : props.type === "404" ?
                                <strong className="mr-auto">HTTP-404</strong> : props.type === "Unauthorized" ?
                                        <strong className="mr-auto">Unauthorized</strong> : props.type === "ad" ?
                                        <strong className="mr-auto">Access Denied</strong>:
                                            <strong className="mr-auto">Info</strong>
                    }
                </Toast.Header>
                <Toast.Body>
                    {props.message}
                </Toast.Body>
            </Toast>
        </div>
    );
}