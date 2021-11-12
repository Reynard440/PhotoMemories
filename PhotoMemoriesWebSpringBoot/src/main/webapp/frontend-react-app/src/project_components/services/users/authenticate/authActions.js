import * as AT from './authTypes';
import axios from 'axios';

export const authenticateUser = (credentials) => {

  return dispatch => {
      dispatch({
          type: AT.LOGIN_REQUEST
      });
      axios.post("http://localhost:8095/v1/c1/login", credentials)
          .then(res => {
              let access_key = res.data.access_key;
              localStorage.setItem('access_key', access_key);
              localStorage.setItem('userEmail', credentials.get("email"));
              dispatch(success(true));
          })
          .catch(err => {
              dispatch(failure(err.message));
          });
  };
};

export const logoutUser = () => {
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

