import * as photoTypes from './photosTypes';
import axios from 'axios';

export const savePhoto = photo => {
  return dispatch => {
      dispatch(savePhotoRequest());
      axios.post("http://localhost:8095/photo-memories/mvc/v1/c2/addNewPhoto", photo)
          .then(res => {
              dispatch(photoSuccess(res.data.cargo));
          })
          .catch(err => {
             dispatch(photoFailure(err));
          });
  };
};

const savePhotoRequest = () => {
    return {
        type: photoTypes.SAVE_PHOTO_REQUEST
    };
};

const updatePhotoRequest = () => {
    return {
        type: photoTypes.UPDATE_PHOTO_REQUEST
    };
};

export const updatePhoto = photo => {
    return dispatch => {
        dispatch(updatePhotoRequest());
        axios.put("http://localhost:8095/photo-memories/mvc/v1/c2/updateMetadata/", photo)
            .then(res => {
                dispatch(photoSuccess(res.data.cargo));
            })
            .catch(err => {
                dispatch(photoFailure(err));
            });
    };
};

const getPhotoRequest = () => {
    return {
        type: photoTypes.GET_PHOTO_REQUEST
    };
};

export const getPhoto = photoId => {
    return dispatch => {
        dispatch(getPhotoRequest());
        axios.get("http://localhost:8095/photo-memories/mvc/v1/c2/getPhotoById/" + photoId)
            .then(res => {
                dispatch(photoSuccess(res.data.cargo));
            })
            .catch(err => {
                dispatch(photoFailure(err));
            });
    };
};

const deletePhotoRequest = () => {
    return {
        type: photoTypes.DELETE_PHOTO_REQUEST
    };
};

export const deletePhoto = (photoId, photoLink) => {
    return dispatch => {
        dispatch(deletePhotoRequest());
        axios.delete("http://localhost:8095/photo-memories/mvc/v1/c2/deletePhoto/"+photoLink+"/reynardengels@gmail.com/"+photoId)
            .then(res => {
                dispatch(photoSuccess(res.data.cargo));
            })
            .catch(err => {
                dispatch(photoFailure(err));
            });
    };
};

const photoSuccess = photo => {
    return {
        type: photoTypes.PHOTO_SUCCESS,
        payload: photo
    };
};

const photoFailure = error => {
    return {
        type: photoTypes.PHOTO_FAILURE,
        payload: error
    };
};