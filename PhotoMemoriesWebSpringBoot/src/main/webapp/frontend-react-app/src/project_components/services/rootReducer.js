import {combineReducers} from 'redux';
import photoReducer from './photos/photoReducer';
import photosReducer from './photos/photosReducer';

const rootReducer = combineReducers({
    photos: photoReducer,
    photo: photosReducer
});

export default rootReducer;