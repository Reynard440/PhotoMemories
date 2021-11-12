import {applyMiddleware, createStore} from 'redux';
import thunk from 'redux-thunk';
import rootReducer from './rootReducer';
import accessKey from "./utilities/accessKey";

const store = createStore(rootReducer, applyMiddleware(thunk));

if (localStorage.access_key) {
    accessKey(localStorage.access_key);
}

export default store;

