import {FAILURE, LOGIN_REQUEST, SUCCESS} from './authTypes';

export const authenticateUser = (email, password) => {
  return dispatch => {
      dispatch(loginRequest());
      if (email === "reynardengels@gmail.com" && password ==="King6") {
          dispatch(success());
      } else {
          dispatch(failure());
      }
  };
};

const loginRequest = () => {
    return {
        type: LOGIN_REQUEST
    };
};

const success = () => {
    return {
        type: SUCCESS,
        payload: true
    };
};

const failure = () => {
    return {
        type: FAILURE,
        payload: false
    };
};

