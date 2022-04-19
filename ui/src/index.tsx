import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { Auth0Provider } from "@auth0/auth0-react";
import configData from "./config.json";
import {BrowserRouter} from "react-router-dom";


ReactDOM.render(
  <BrowserRouter>
    <Auth0Provider     
      {...configData}
      cacheLocation="localstorage"
      redirect_uri="window.location.origin"
      useRefreshTokens={true}  
      >
        <>
            <App />
        </>
    </Auth0Provider>
  </BrowserRouter>,
  document.getElementById('root')
);