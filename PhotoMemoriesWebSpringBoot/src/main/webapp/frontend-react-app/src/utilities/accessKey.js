import axios from 'axios';

export default function accessKey(access_key) {
    if (access_key) {
        axios.defaults.headers.common['Authorization'] = `${access_key}`;
    } else {
        delete axios.defaults.headers.common['Authorization'];
    }
};
