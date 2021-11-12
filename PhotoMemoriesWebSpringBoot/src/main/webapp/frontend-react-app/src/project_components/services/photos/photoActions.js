import {GET_PHOTO_FAILURE, GET_PHOTO_REQUEST, GET_PHOTO_SUCCESS} from './photoTypes';
import axios from 'axios';

export const getPhotos = () => {
    return dispatch => {
        dispatch(getPhotoRequest());
        axios.get("http://localhost:8095/v1/c2/loadAllPhotosOfUser/"+localStorage.userEmail+"/")
            .then(res => {
                dispatch(getPhotoSuccess(res.data.cargo));
            })
            .catch(error => {
                dispatch(getPhotoFailure(error.message));
            });
    };
};

const getPhotoRequest = () => {
    return {
        type: GET_PHOTO_REQUEST
    };
};

const getPhotoSuccess = photo => {
    return {
        type: GET_PHOTO_SUCCESS,
        payload: photo
    };
};

const getPhotoFailure = error => {
    return {
        type: GET_PHOTO_FAILURE,
        payload: error
    };
};