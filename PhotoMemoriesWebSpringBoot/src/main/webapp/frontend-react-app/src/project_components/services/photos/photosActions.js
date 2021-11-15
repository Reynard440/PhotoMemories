import * as photoTypes from './photosTypes';
import axios from 'axios';

export const savePhoto = photo => {
  return dispatch => {
      dispatch(savePhotoRequest());
      axios.post("http://localhost:8095/v1/c2/addNewPhoto", photo,
          {
              headers:{
                  "Access-Control-Allow-Origin": "*",
                  "Authorization": localStorage.access_key
              }
          })
          .then(res => {
              dispatch(photoSuccess(res.data.cargo));
          })
          .catch(err => {
             dispatch(photoFailure(err));
              console.log(err.message);
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
        axios.put("http://localhost:8095/v1/c2/updateMetadata/" + photo.get("photoId"), photo,
            {
                headers:{
                    "Access-Control-Allow-Origin": "*",
                    "Authorization": localStorage.access_key
                }
            })
            .then(res => {
                dispatch(photoSuccess(res.data.cargo));
            })
            .catch(err => {
                dispatch(photoFailure(err.message));
                console.log(err.message);
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
        axios.get("http://localhost:8095/v1/c2/getPhotoById/" + photoId,
            {
                headers:{
                    "Access-Control-Allow-Origin": "*",
                    "Authorization": localStorage.access_key
                }
            })
            .then(res => {
                dispatch(photoSuccess(res.data.cargo));
            })
            .catch(err => {
                dispatch(photoFailure(err.message));
                console.log(err.message);
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
        axios.delete("http://localhost:8095/v1/c2/deletePhoto/"+photoLink + "/" + localStorage.userEmail + "/" + photoId,
            {
                headers:{
                    "Access-Control-Allow-Origin": "*",
                    "Authorization": localStorage.access_key
                }
            })
            .then(res => {
                dispatch(photoSuccess(res.data.cargo));
            })
            .catch(err => {
                dispatch(photoFailure(err.message));
                console.log(err.message);
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