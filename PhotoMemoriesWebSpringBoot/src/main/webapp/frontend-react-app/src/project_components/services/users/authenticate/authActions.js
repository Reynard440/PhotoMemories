import * as AT from './authTypes';
import axios from 'axios';

export const authenticateUser = (credentials) => {
    return dispatch => {
        dispatch({
            type: AT.LOGIN_REQUEST
        });
        axios.post("http://localhost:8095/v1/c1/login", credentials)
            .then(res => {
                let access_key = res.data.access_token;
                localStorage.setItem('access_key', "Carrier " + access_key);
                localStorage.setItem('userEmail', credentials.get("email"));
                dispatch(success(true));
            })
            .catch(err => {
                dispatch(failure(err.message));
            });
    };
};

export const addUser = (newUser) => {
    return dispatch => {
        dispatch(registerRequest());
        console.log(newUser.fname);
        axios.post("http://localhost:8095/v1/c1/addNewUser", newUser)
            .then(res => {
                dispatch({
                    type: AT.SUCCESS,
                    payload: res.data.cargo
                });
            })
            .catch(err => {
               dispatch(failure(err.message));
            });
    };
};

const registerRequest = () => {
    return {
        type: AT.REGISTER_USER_REQUEST
    };
};

export const logoutUser = (logoutUser) => {
    return dispatch => {
        dispatch({
            type: AT.LOGOUT_REQUEST
        });
        localStorage.removeItem('access_key');
        dispatch(success(false));
    };
};

const success = isLoggedIn => {
    return {
        type: AT.SUCCESS,
        payload: isLoggedIn
    };
};

const failure = () => {
    return {
        type: AT.FAILURE,
        payload: false
    };
};

