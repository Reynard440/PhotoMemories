import {FAILURE, LOGIN_REQUEST, LOGOUT_REQUEST, SUCCESS} from './authTypes';

const initialState = {
    isLoggedIn: ''
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case LOGIN_REQUEST:
        case LOGOUT_REQUEST:
            return {
                ...state
            };
        case SUCCESS:
        case FAILURE:
            return {
                isLoggedIn: action.payload
            };
        default:
            return state;
    }
};

export default reducer;