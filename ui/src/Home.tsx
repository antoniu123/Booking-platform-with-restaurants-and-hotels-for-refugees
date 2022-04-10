import React from "react";
import {Breadcrumb, Layout, Menu, Image} from "antd";
import { User } from '@auth0/auth0-spa-js';
import {LaptopOutlined, NotificationOutlined, UserOutlined} from "@ant-design/icons";

const { SubMenu } = Menu;
const { Header, Content, Sider } = Layout;

export interface HomeProps {
    user: User|undefined
    roles: string
    logout: () => void
    message: string
    action: ()=> void
}

export const Home : React.VFC<HomeProps> = ({action, user, roles, logout, message}) => {
    return (
        <>
            <Layout>
                <Header className="header">
                    <div className="logo" />
                    <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']}>
                        <Menu.Item key="1">Hotels</Menu.Item>
                        <Menu.Item key="2">Restaurant</Menu.Item>
                        <Menu.Item key="3">Ask for help</Menu.Item>
                        <Menu.Item key="4" onClick={logout}>Logout</Menu.Item>
                    </Menu>
                </Header>
                <p className="ant-divider-with-text">Hi {user ? user.email : ''}, You have successfully logged in as {roles}</p>
                <Layout>
                    <Sider width={200} className="site-layout-background">
                        <Menu
                            mode="inline"
                            defaultSelectedKeys={['1']}
                            defaultOpenKeys={['sub1']}
                            style={{ height: '100%', borderRight: 0 }}
                        >
                            <SubMenu key="sub1" icon={<UserOutlined />} title="subnav 1">
                                <Menu.Item key="1">option1</Menu.Item>
                                <Menu.Item key="2">option2</Menu.Item>
                                <Menu.Item key="3">option3</Menu.Item>
                                <Menu.Item key="4">option4</Menu.Item>
                            </SubMenu>
                            <SubMenu key="sub2" icon={<LaptopOutlined />} title="subnav 2">
                                <Menu.Item key="5">option5</Menu.Item>
                                <Menu.Item key="6">option6</Menu.Item>
                                <Menu.Item key="7">option7</Menu.Item>
                                <Menu.Item key="8">option8</Menu.Item>
                            </SubMenu>
                            <SubMenu key="sub3" icon={<NotificationOutlined />} title="subnav 3">
                                <Menu.Item key="9">option9</Menu.Item>
                                <Menu.Item key="10">option10</Menu.Item>
                                <Menu.Item key="11">option11</Menu.Item>
                                <Menu.Item key="12">option12</Menu.Item>
                            </SubMenu>
                        </Menu>
                    </Sider>
                    <Layout style={{ padding: '0 24px 24px' }}>
                        <Breadcrumb style={{ margin: '16px 0' }}>
                            <Breadcrumb.Item>Home</Breadcrumb.Item>
                            <Breadcrumb.Item>List</Breadcrumb.Item>
                            <Breadcrumb.Item>App</Breadcrumb.Item>
                        </Breadcrumb>
                        <Content
                            className="site-layout-background"
                            style={{
                                padding: 24,
                                margin: 0,
                                minHeight: 280,
                            }}
                        >
                            <h1 className="font-medium leading-tight text-2xl mt-0 mb-2 text-blue-600">
                                Hello world!
                            </h1>
                            {/*<Button type="primary" onClick={() => action()}>Test Private API</Button>*/}
                            {/*{*/}
                            {/*    message ?*/}
                            {/*        <p>Response Message: {message}</p> : ''*/}
                            {/*}*/}
                            <Image
                                preview={false}
                                src="background.jpg"
                            />
                        </Content>
                    </Layout>
                </Layout>
            </Layout>
        </>
    )
}