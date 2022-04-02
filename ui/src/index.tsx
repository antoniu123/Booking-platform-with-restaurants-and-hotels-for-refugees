import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { Auth0Provider } from "@auth0/auth0-react";
import configData from "./config.json";


ReactDOM.render(
  <React.StrictMode>
    <Auth0Provider     
      {...configData}
      cacheLocation="localstorage"
      redirect_uri="window.location.origin"
      useRefreshTokens={true}  
      >
      <App />
    </Auth0Provider>
  </React.StrictMode>,
  document.getElementById('root')
);