import {
    DELETE_PHOTO_REQUEST,
    GET_PHOTO_REQUEST,
    PHOTO_FAILURE,
    PHOTO_SUCCESS,
    SAVE_PHOTO_REQUEST,
    UPDATE_PHOTO_REQUEST
} from './photosTypes';

const initState = {
    photo:'', error:''
};

const reducer = (state = initState, action) => {
    switch (action.type) {
        case SAVE_PHOTO_REQUEST || UPDATE_PHOTO_REQUEST || DELETE_PHOTO_REQUEST || GET_PHOTO_REQUEST:
            return {
                ...state
            };
        case PHOTO_SUCCESS:
            return {
                photos: action.payload,
                error: ''
            };
        case PHOTO_FAILURE:
            return {
                photos: '',
                error: action.payload
            };
        default:
            return state;
    };
};

export default reducer;