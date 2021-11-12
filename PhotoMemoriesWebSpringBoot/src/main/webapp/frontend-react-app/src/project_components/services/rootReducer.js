import {combineReducers} from 'redux';
import photoReducer from './photos/photoReducer';
import photosReducer from './photos/photosReducer';
import authReducer from './users/authenticate/authReducer';

const rootReducer = combineReducers({
    photos: photoReducer,
    photo: photosReducer,
    auth: authReducer
});

export default rootReducer;