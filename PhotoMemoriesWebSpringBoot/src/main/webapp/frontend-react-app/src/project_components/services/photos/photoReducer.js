import {GET_PHOTO_FAILURE, GET_PHOTO_REQUEST, GET_PHOTO_SUCCESS} from './photoTypes';

const  initState = {
    photos: [],
    error: ''
};

const reducer = (state = initState, action) => {
    switch (action.type) {
        case GET_PHOTO_REQUEST:
            return {
                ...state
            };
        case GET_PHOTO_SUCCESS:
            return {
                photos: action.payload,
                error: ''
            };
        case GET_PHOTO_FAILURE:
            return {
                photos: [],
                error: action.payload
            };
        default:
            return state;
    };
};

export default reducer;