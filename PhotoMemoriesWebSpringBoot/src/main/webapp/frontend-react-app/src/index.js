import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import {Provider} from "react-redux";
import store from './project_components/services/store';

// export * from './users/authenticate/authActions';

ReactDOM.render(
  <React.StrictMode>
      <Provider store={store}><App /></Provider>
  </React.StrictMode>,
  document.getElementById('root')
);

reportWebVitals();
