import "./App.css";
import React, {useState, useEffect} from "react";
import { useAuth0 } from "@auth0/auth0-react";
import configData from "./config.json";
import {Avatar, Button, Layout, Menu, Spin} from "antd";
import {DashboardOutlined, FormOutlined, LoadingOutlined, UserOutlined} from '@ant-design/icons'
import jwtDecode from "jwt-decode"
import { TokenDecoded } from "./model/TokenDecoded";
import {Home} from "./Home";
import {Route, Routes, Link} from 'react-router-dom'
import {Help} from "./pages/Help";
import { User } from '@auth0/auth0-spa-js';
import Hotels from "./pages/Hotels";
import Reservations from "./pages/Reservations";
import Restaurants from "./pages/Restaurants";
import MenuDetailsManage from "./pages/MenuDetailsManage";

const { SubMenu } = Menu;
const {Content, Sider } = Layout;
const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

export interface UserContextInterface {
  user: User | undefined,
  accessToken: string,
  role: string
}

export const UserContext = React.createContext<UserContextInterface | null>(null);

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
          //console.log(resJson)
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
    //console.log(decoded)
    const roles : string[] = decoded['http://schemas.microsoft.com/ws/2008/06/identity/claims/role']
    return roles.toString()
  }

  return (
    <>
    { isLoading ?
      <><Spin size="large" indicator={antIcon} /> </> :
      <>
        {(isAuthenticated && accessToken) &&
          ( <>
            <UserContext.Provider value={
              {user: user,
               accessToken: accessToken,
               role: getRole(accessToken)
              }
            }>
              <Layout>
                <p className="ant-divider-with-text">Hi {user ? user.email : ''}, You have successfully logged in as {getRole(accessToken)}</p>
                <Layout>
                  <Sider width={200} className="site-layout-background">
                    <Menu
                        mode="inline"
                        style={{ height: '100%', borderRight: 0 }}
                    >
                      <Menu.Item key="8">
                        <Link to="/">Home</Link>
                      </Menu.Item>
                      <SubMenu key="sub1" icon={<DashboardOutlined />} title="Dashboard">
                        {(getRole(accessToken) === "ADMIN" || getRole(accessToken) === "USER") &&
                            <>
                              <Menu.Item key="1">
                                <Link to="/hotels">Hotels</Link>
                              </Menu.Item>
                              <Menu.Item key="2">
                                <Link to="/restaurants">Restaurants</Link>
                              </Menu.Item>
                            </>}
                        {getRole(accessToken) === "USER" &&
                            <Menu.Item key="3">
                                <Link to="/help">Ask for help</Link>
                            </Menu.Item>}
                        {getRole(accessToken) === "USER" &&
                            <Menu.Item key="9">
                              <Link to="/reservations">My reservations</Link>
                            </Menu.Item>}
                        {getRole(accessToken) === "ADMIN" &&
                        <Menu.Item key="1">
                          <Link to="/menu">Menu</Link>
                        </Menu.Item>}
                        {getRole(accessToken) === "HELPER" &&
                            <Menu.Item key="6">Offer help</Menu.Item>}
                      </SubMenu>
                      {getRole(accessToken) === "USER" &&
                          <Menu.Item key="4" icon={<FormOutlined/>}>Customer support</Menu.Item>}
                      {getRole(accessToken) === "USER" &&
                          <SubMenu key="sub3" icon={<Avatar size={"small"} style={{ backgroundColor: '#87d068' }} icon={<UserOutlined/>} />} title="Profile">
                            <Menu.Item key="5">Edit profile</Menu.Item>
                          </SubMenu>}
                      <Menu.Item key="7" onClick={()=>logout({ returnTo: window.location.origin })}>Logout</Menu.Item>
                    </Menu>
                  </Sider>
                  <Layout style={{ padding: '0 24px 24px' }}>
                    <Content
                        className="site-layout-background"
                        style={{
                          padding: 24,
                          margin: 0,
                          minHeight: 280,
                        }}
                    >
                      {/*<Button type="primary" onClick={() => action()}>Test Private API</Button>*/}
                      {/*{*/}
                      {/*    message ?*/}
                      {/*        <p>Response Message: {message}</p> : ''*/}
                      {/*}*/}

                      <Routes>
                        <Route path='/' element={<Home/>}/>
                        <Route path='/hotels' element={<Hotels/>}/>
                        <Route path='/restaurants' element={<Restaurants/>}/>
                        <Route path='/reservations' element={<Reservations/>}/>
                        <Route path='/help' element={<Help/>}/>
                        <Route path='/menu' element={<MenuDetailsManage/>}/>
                      </Routes>
                    </Content>
                  </Layout>
                </Layout>
              </Layout>
            </UserContext.Provider>
            </>)
        }

        {!isAuthenticated &&
          (
            <div className="App">
              <header className="App-header">
                <img src= "background.jpg" alt="background" />
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
