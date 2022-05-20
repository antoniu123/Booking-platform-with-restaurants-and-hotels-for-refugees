import "./App.css";
import React, {useEffect, useState} from "react";
import {useAuth0} from "@auth0/auth0-react";
import configData from "./config.json";
import {Avatar, Button, Layout, Menu, Spin} from "antd";
import {DashboardOutlined, FormOutlined, LoadingOutlined, UserOutlined} from '@ant-design/icons'
import jwtDecode from "jwt-decode"
import {TokenDecoded} from "./model/TokenDecoded";
import {Home} from "./pages/Home";
import {Link, Route, Routes} from 'react-router-dom'
import {Help} from "./pages/help/Help";
import {User} from '@auth0/auth0-spa-js';
import Hotels from "./pages/hotel/Hotels";
import Reservations from "./pages/hotel/Reservations";
import Restaurants from "./pages/restaurant/Restaurants";
import MenuDetailsManage from "./pages/order/MenuDetailsManage";
import OfferHelp from "./pages/help/OfferHelp";
import CustomerSupport from "./pages/other/CustomerSupport";
import Orders from "./pages/order/Orders";
import EditProfile from "./pages/other/EditProfile";
import {Header} from "antd/es/layout/layout";

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


  const [accessToken, setAccessToken] = useState<string>(undefined as unknown as string)
  const [, setAPIResponseMessage] = useState('')
  const [showOfferHelp, setShowOfferHelp] = useState(false)
  const [showCustomerSupport, setShowCustomerSupport] = useState(false)


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
      <>
        <Spin size="large" indicator={antIcon} />
      </> :
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
                  <Sider breakpoint="lg"
                         collapsedWidth="0"
                         className="site-layout-background">
                    <Menu
                        mode="inline"
                        style={{ height: '100%', borderRight: 0 }}
                    >
                      <Menu.Item key="0">
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
                            <Menu.Item key="4">
                              <Link to="/reservations">My reservations</Link>
                            </Menu.Item>}
                        {getRole(accessToken) === "USER" &&
                            <Menu.Item key="5">
                              <Link to="/orders">My completed orders</Link>
                            </Menu.Item>}
                        {getRole(accessToken) === "ADMIN" &&
                            <Menu.Item key="6">
                              <Link to="/reservations">Reservations</Link>
                            </Menu.Item>}
                        {getRole(accessToken) === "ADMIN" &&
                            <Menu.Item key="7">
                              <Link to="/orders">Completed orders</Link>
                            </Menu.Item>}
                        {getRole(accessToken) === "ADMIN" &&
                        <Menu.Item key="8">
                          <Link to="/menu">Menu</Link>
                        </Menu.Item>}
                        {getRole(accessToken) === "ADMIN" &&
                            <Menu.Item key="9">
                              <Link to="/offerHelp" onClick={()=>setShowOfferHelp(true)}>Offer Help</Link>
                            </Menu.Item>}
                      </SubMenu>
                      {getRole(accessToken) === "USER" &&
                          <Menu.Item key="10" icon={<FormOutlined/>} >
                            <Link to="/support" onClick={()=>setShowCustomerSupport(true)}>Customer support</Link>
                          </Menu.Item>}
                      {getRole(accessToken) === "USER" &&
                          <SubMenu key="11" icon={<Avatar size={"small"} style={{ backgroundColor: '#87d068' }} icon={<UserOutlined/>} />} title="Profile">
                            <Menu.Item>
                              <Link to="/editProfile" >Edit profile</Link>
                            </Menu.Item>
                          </SubMenu>}
                      <Menu.Item key="12" onClick={()=>logout({ returnTo: window.location.origin })}>Logout</Menu.Item>
                    </Menu>
                  </Sider>
                  <Layout >
                    <Header className="header">
                      <p> Hi {user ? user.email : ''} [{getRole(accessToken)}]</p>
                    </Header>
                    <Content
                        className="site-layout-background"
                        style={{
                          padding: 0,
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
                        <Route path='/support' element={<CustomerSupport close={()=>setShowCustomerSupport(false)}
                                                                         visible={showCustomerSupport}/>}/>
                        <Route path='/orders' element={<Orders/>}/>
                        <Route path='/offerHelp' element={<OfferHelp
                         close={()=>setShowOfferHelp(false)} visible={showOfferHelp}/>}/>
                        <Route path='/editProfile' element={<EditProfile />}/>
                      </Routes>
                    </Content>
                  </Layout>
                </Layout>
            </UserContext.Provider>
            </>)
        }

        {!isAuthenticated &&
          (
            <div className="App">
              <header className="App-header">
                <img src= "bckg.jpeg" width={"1000"} alt="background" />
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
