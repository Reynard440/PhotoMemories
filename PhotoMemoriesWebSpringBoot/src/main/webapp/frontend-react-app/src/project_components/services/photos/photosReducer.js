import * as photoTypes from './photosTypes';

const initState = {
    photo:'', error:''
};

const reducer = (state = initState, action) => {
    switch (action.type) {
        case photoTypes.SAVE_PHOTO_REQUEST:
        case photoTypes.UPDATE_PHOTO_REQUEST:
        case photoTypes.DELETE_PHOTO_REQUEST:
        case photoTypes.GET_PHOTO_REQUEST:
            return {
                ...state
            };
        case photoTypes.PHOTO_SUCCESS:
            return {
                photos: action.payload,
                error: ''
            };
        case photoTypes.PHOTO_FAILURE:
            return {
                photos: '',
                error: action.payload
            };
        default:
            return state;
    };
};

export default reducer;