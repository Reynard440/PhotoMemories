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
            <Toast className={`border text-white ${props.type === "success" ? "border-success bg-success" : "danger" ? "border-danger bg-danger" : "border-info bg-info"}`} show={props.show}>
                <Toast.Header className={`text-white ${props.type === "success" ? "bg-success" : "danger" ? "bg-danger" : "bg-info"}`} closeButton={false}>
                    <strong className="mr-auto">Success</strong>
                </Toast.Header>
                <Toast.Body>
                    {props.message}
                </Toast.Body>
            </Toast>
        </div>
    );
}