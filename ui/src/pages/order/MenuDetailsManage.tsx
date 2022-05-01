import React, {useState} from "react";
import {assign, Machine} from "xstate";
import axios from "axios";
import {useMachine} from "@xstate/react";
import {Alert, Button, Result, Spin, Table} from 'antd';
import {MenuRestaurant} from "../../model/MenuRestaurant";
import {UserContext, UserContextInterface} from "../../App";
import AddEditMenuDetail from "./AddEditMenuDetail";
import {displayNotification} from "../../shared/displayNotification";


const MenuDetailsManage: React.FC = () => {

    const value: UserContextInterface | null = React.useContext(UserContext)
    const [menuRestaurantState, send] = useMachine(createMenuRestaurantMachine(value))
    const [menuRestaurantId, setMenuRestaurantId] = useState(0);
    const [addEditVisible, setAddEditVisible] = useState(false);

    const refresh = () => {
        send({
            type: 'RETRY'
        })
    }

    const columns = [
        {
            title: '#',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Restaurant Name',
            dataIndex: 'restaurantName',
            key: 'restaurantName',
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'Image',
            key: 'image',
            render:  (record:MenuRestaurant) => <img width="120px" src={record.image}  alt={"img"}/>

        },
        {
            title: 'Edit',
            key: 'edit',
            hidden: value?.role.toString() !== "ADMIN",
            render: (record: MenuRestaurant) => (
                <Button hidden={value?.role.toString() !== "ADMIN"} type="primary" ghost onClick={
                    () => {
                        setMenuRestaurantId(record.id)
                        setAddEditVisible(true)
                    }
                }> Edit </Button>
            )
        },
        {
            title: 'Delete',
            key: 'delete',
            hidden: value?.role.toString() !== "ADMIN",
            render: (record: MenuRestaurant) => (
                <Button hidden={value?.role.toString() !== "ADMIN"} danger onClick={
                    () => {
                        send({
                            type: 'DELETE', payload: {menuRestaurantId: record.id}
                        })
                    }
                }> Delete </Button>
            )
        }
    ].filter(item => !item.hidden);

    return (
        <div>
            {(menuRestaurantState.matches('loadingMenuRestaurantData') ||
                menuRestaurantState.matches('deletingMenuRestaurantData')) && (
                <>
                    <Spin>
                        <Alert message="Please wait for loading" type="info"/>
                    </Spin>
                </>
            )}

            {menuRestaurantState.matches('loadMenuRestaurantDataResolved') && (
                <>
                    <p className={"center_text"}>These are our menuRestaurant options</p>
                    {value?.role.toString() === "ADMIN" &&
                        <Button type="primary" onClick={
                            () => {
                                setMenuRestaurantId(0)
                                setAddEditVisible(true)
                            }
                        }>Add
                        </Button>
                    }
                    <Table dataSource={menuRestaurantState.context.menuRestaurants} columns={columns}/>
                    <AddEditMenuDetail
                                  key={menuRestaurantId}
                                  menuRestaurantId={menuRestaurantId}
                                  visible={addEditVisible}
                                  onSubmit={() => setAddEditVisible(false)}
                                  onCancel={() => setAddEditVisible(false)}
                                  onRefresh={() => refresh()}
                    />
                </>
            )}

            {menuRestaurantState.matches('loadMenuRestaurantDataRejected') && (
                <>
                    <Result
                        status="error"
                        title="Loading failed"
                        //description="Please check and modify the following information before resubmitting."
                        extra={<Button size="large" type="primary" onClick={() => {
                            send({
                                type: 'RETRY'
                            })
                        }}>Try Again</Button>}
                    />
                </>
            )}



        </div>
    )

}

export default MenuDetailsManage

interface MenuRestaurantMachineContext {
    menuRestaurants: Array<MenuRestaurant>
}

interface MenuRestaurantMachineSchema {
    context: MenuRestaurantMachineContext
    states: {
        loadingMenuRestaurantData: {}
        loadMenuRestaurantDataResolved: {}
        loadMenuRestaurantDataRejected: {}
        deletingMenuRestaurantData: {}
    }
}

type MenuRestaurantMachineEvent = | { type: 'RETRY' }
    | { type: 'CANCEL' }
    | { type: 'DELETE'; payload: { menuRestaurantId: number } }

const createMenuRestaurantMachine = (userContext: UserContextInterface | null) => Machine<MenuRestaurantMachineContext, MenuRestaurantMachineSchema, MenuRestaurantMachineEvent>(
    {
        id: 'menuRestaurant-machine',
        context: {
            menuRestaurants: [],
        },
        initial: 'loadingMenuRestaurantData',
        on: {
            RETRY: 'loadingMenuRestaurantData'
        },
        states: {
            loadingMenuRestaurantData: {
                invoke: {
                    id: 'loadingMenuRestaurantData',
                    src: 'loadMenuRestaurantData',
                    onDone: {
                        target: 'loadMenuRestaurantDataResolved',
                        actions: assign((context, event) => {
                            return {
                                menuRestaurants: event.data.data
                            }
                        })
                    },
                    onError: {
                        target: 'loadMenuRestaurantDataRejected'
                    }
                }
            },
            loadMenuRestaurantDataResolved: {
                on: {
                    RETRY: {
                        target: 'loadingMenuRestaurantData'
                    },
                    DELETE: {
                        target: 'deletingMenuRestaurantData'
                    }
                }
            },
            loadMenuRestaurantDataRejected: {
                on: {
                    RETRY: {
                        target: 'loadingMenuRestaurantData'
                    }
                }
            },
            deletingMenuRestaurantData: {
                invoke: {
                    id: 'deletingMenuRestaurantData',
                    src: 'deleteMenuRestaurantData',
                    onDone: {
                        target: 'loadingMenuRestaurantData',
                        actions: 'ok'
                    },
                    onError: {
                        target: 'loadingMenuRestaurantData',
                        actions: 'error'
                    }
                }
            }
        }
    },
    {
        actions: {
            ok: () => {
                displayNotification('Info','Saving has been done', 1)
            },
            error: (context,event) => {
                displayNotification('Error',  'Error at save', 1)
            }
        },
        services: {
            loadMenuRestaurantData: () => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/menuRestaurant/all`
                return axios
                    .get(url, {headers: {"Authorization": `Bearer ${token}`, "Content-Type": "application/json"}})
                    .then((ret) => Promise.resolve(ret)
                    )
                    .catch((err) => {
                        return Promise.reject(err)
                    })
            },
            deleteMenuRestaurantData: (id, event) => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/menuRestaurant/delete/${event.type === 'DELETE' ? event.payload.menuRestaurantId : 0}`
                return axios
                    .delete(url, {headers: {"Authorization": `Bearer ${token}`, "Content-Type": "application/json"}})
                    .then((ret) => Promise.resolve(ret)
                    )
                    .catch((err) => {
                        return Promise.reject(err)
                    })
            }
        }
    }
)
