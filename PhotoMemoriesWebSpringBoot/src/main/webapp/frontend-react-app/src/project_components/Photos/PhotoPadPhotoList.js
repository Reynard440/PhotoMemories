import React, {Component} from "react";
import {Card, Table} from "react-bootstrap";
import CardHeader from "react-bootstrap/CardHeader";

export default class PhotoPadPhotoList extends Component {
    render(){
        return (
            <Card className={"border border-secondary bg-secondary text-white"}>
                <CardHeader>Photo List</CardHeader>
                <Card.Body>
                    <Table bordered hover striped variant={"dark"}>
                        <tr>
                            <th>#</th>
                            <th>Date Modified</th>
                            <th>Captured By</th>
                            <th>Format</th>
                            <th>Location</th>
                            <th>Name</th>
                            <th>Size</th>
                            <th>Date Uploaded</th>
                        </tr>
                        <tbody>
                        <tr>
                            <th>1</th>
                            <th>2012-02-10</th>
                            <th>Reyno Engels</th>
                            <th>jpeg</th>
                            <th>Vaalpark</th>
                            <th>ReynardEngels.jpeg</th>
                            <th>1113487</th>
                            <th>2021-10-28</th>
                        </tr>
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        );
    }
}
