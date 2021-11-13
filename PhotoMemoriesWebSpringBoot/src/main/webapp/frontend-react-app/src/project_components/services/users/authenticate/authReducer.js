import * as AUTH from './authTypes';

const initialState = {
    isLoggedIn: ''
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case AUTH.LOGIN_REQUEST:
        case AUTH.LOGOUT_REQUEST:
            return {
                ...state
            };
        case AUTH.REGISTER_USER_REQUEST:
            return {
                message: action.payload,
                error: ''
            };
        case AUTH.SUCCESS:
        case AUTH.FAILURE:
            return {
                isLoggedIn: action.payload
            };
        default:
            return state;
    }
};

export default reducer;