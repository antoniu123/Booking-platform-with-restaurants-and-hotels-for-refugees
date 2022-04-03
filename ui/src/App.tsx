import "./App.css";
import { useState, useEffect } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import configData from "./config.json";
import logo from "./logo.svg";
import { Button, Card,  Spin } from "antd";
import { LoadingOutlined } from '@ant-design/icons'
import jwtDecode from "jwt-decode"
import { TokenDecoded } from "./model/TokenDecoded";
import {Home} from "./Home";

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

const App: React.VFC = () => {
  
  const {
    isLoading,
    isAuthenticated,
    user,
    getAccessTokenSilently,
    loginWithPopup,
    logout,
  } = useAuth0();

  const [accessToken, setAccessToken] = useState<string>(undefined as unknown as string);
  const [apiResponseMessage, setAPIResponseMessage] = useState('');

  useEffect(() => {
    setAPIResponseMessage('');
    const getAccessToken = async () => {
      try {
        const accessToken = await getAccessTokenSilently({
          audience: configData.audience
        });
        setAccessToken(accessToken);
      } catch (e:any) {
        console.log(e.message);
      }
    };
    if (!isLoading)
       getAccessToken();
  }, [getAccessTokenSilently, setAPIResponseMessage, isLoading]);

  const login = async () =>{
    await loginWithPopup()
  }

  const securedAPITest = () => {
    if (accessToken)
      fetch("http://localhost:8080/auth0/private", {
        method: "GET",
        headers: new Headers({
          Authorization: "Bearer " + accessToken,
          "Content-Type": "application/json",
        }),
      })
        .then(function (res) {
          return res.json();
        })
        .then(function (resJson) {
          console.log(resJson)
          setAPIResponseMessage(resJson.message);
        })
        .catch((e) => console.log(e));
    else {
      console.warn("no token")
    }    

  };

  const getRole = (token:string|undefined) => {
    if (!token)
       return ''
    const decoded = jwtDecode<TokenDecoded>(token)
    console.log(decoded)
    const roles : string[] = decoded['http://schemas.microsoft.com/ws/2008/06/identity/claims/role']
    return roles.toString()
  }

  return (
    <>
    { isLoading ?
      <><Spin size="large" indicator={antIcon} /> </> :
      <>
        {isAuthenticated &&          
          ( <>
              <Home roles={getRole(accessToken)}
                    user={user}
                    logout={()=>logout({ returnTo: window.location.origin })}
                    message={apiResponseMessage} action={() => securedAPITest()}/>
            </>)
        }
        {!isAuthenticated &&
          (
            <div className="App">
              <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />                 
              </header>
              <Button type="primary" onClick={() => login()}>
                        Log in
              </Button>
            </div>      
          )
        }
      </>
    }
    </>
  )
}

export default App;
