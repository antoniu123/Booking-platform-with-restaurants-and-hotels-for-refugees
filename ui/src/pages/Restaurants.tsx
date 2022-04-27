import React, {useRef, useState} from "react";
import {assign, Machine} from "xstate";
import axios from "axios";
import {useMachine} from "@xstate/react";
import {Alert, Button, Modal, Result, Spin, Table} from 'antd';
import {UserContext, UserContextInterface} from "../App";
import {Restaurant} from "../model/Restaurant";
import {MenuRestaurant} from "../model/MenuRestaurant";
import {Hotel} from "../model/Hotel";
import AddEditRestaurant from "./AddEditRestaurant";
import ViewRestaurant from "./ViewRestaurant";
import {Order} from "../model/Order";
import PickQuantity from "./PickQuantity";


const Restaurants: React.FC = () => {

    const value: UserContextInterface | null = React.useContext(UserContext)
    const [restaurantState, send] = useMachine(createRestaurantMachine(value))
    const [restaurantId, setRestaurantId] = useState(0);
    const [addEditVisible, setAddEditVisible] = useState(false);
    const [detailVisible, setDetailVisible] = useState(false);
    const [pickVisible, setPickVisible] = useState(false);
    const currentProduct = useRef({} as MenuRestaurant)

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
            title: 'Restaurant name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Detail',
            key: 'detail',
            hidden: value?.role.toString() !== "USER",
            render: (record: Hotel) => (
                <Button hidden={value?.role.toString() !== "USER"} onClick={() => {
                    setRestaurantId(record.id)
                    setDetailVisible(true)
                }
                }> Restaurant Details </Button>
            )
        },
        {
            title: 'Edit',
            key: 'edit',
            hidden: value?.role.toString() !== "ADMIN",
            render: (record: Hotel) => (
                <Button hidden={value?.role.toString() !== "ADMIN"} type="primary" ghost onClick={
                    () => {
                        setRestaurantId(record.id)
                        setAddEditVisible(true)
                    }
                }> Edit </Button>
            )
        },
        {
            title: 'Delete',
            key: 'delete',
            hidden: value?.role.toString() !== "ADMIN",
            render: (record: Hotel) => (
                <Button hidden={value?.role.toString() !== "ADMIN"} danger onClick={
                    () => {
                        send({
                            type: 'DELETE', payload: {restaurantId: record.id}
                        })
                    }
                }> Delete </Button>
            )
        },
        {
            title: 'Menu detail',
            key: 'detail',
            hidden: value?.role.toString() !== "USER",
            render: (record: Restaurant) => (
                <Button hidden={value?.role.toString() !== "USER"} onClick={() => {
                    send({type: 'DETAIL', payload: {restaurant: record}})
                }
                }> Menu Details </Button>
            )
        },

    ].filter(item => !item.hidden)

    const columnsDetail = [
        {
            title: '#',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Order name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
        {
            title: 'image',
            key: 'image',
            render: (record: MenuRestaurant) => <img src={record.image} alt={"img"}/>
        },
        {
            title: 'Pick',
            key: 'pick',
            hidden: value?.role.toString() !== "USER",
            render: (record: MenuRestaurant) => (
                <Button hidden={value?.role.toString() !== "USER"} onClick={() => {
                    setPickVisible(true)
                    currentProduct.current = record
                    send("PICK");
                }
                }> Pick </Button>
            )
        }
    ];

    return (
        <>
            {(restaurantState.matches('loadingRestaurantData') ||
                restaurantState.matches('loadingMenuData') ||
                restaurantState.matches('deletingRestaurantData') ||
                restaurantState.matches('deletingRestaurantData') ||
                restaurantState.matches('submitOrder')) && (
                <>
                    <Spin>
                        <Alert message="Please wait for loading" type="info"/>
                    </Spin>
                </>
            )}

            {restaurantState.matches('loadRestaurantDataResolved') && (
                <>
                    <p className={"center_text"}>These are our restaurants options</p>
                    {value?.role.toString() === "ADMIN" &&
                        <Button type="primary" onClick={
                            () => {
                                setRestaurantId(0)
                                setAddEditVisible(true)
                            }
                        }>Add
                        </Button>
                    }
                    <Table rowKey="id" dataSource={restaurantState.context.restaurants} columns={columns}/>
                    {addEditVisible && <AddEditRestaurant key={restaurantId}
                                                          restaurantId={restaurantId}
                                                          visible={addEditVisible}
                                                          onSubmit={() => setAddEditVisible(false)}
                                                          onCancel={() => setAddEditVisible(false)}
                                                          onRefresh={() => refresh()}
                    />}
                    {detailVisible && <ViewRestaurant key={restaurantId}
                                                      restaurantId={restaurantId}
                                                      visible={detailVisible}
                                                      onCancel={() => setDetailVisible(false)}
                    />}
                </>
            )}

            {restaurantState.matches('loadRestaurantDataRejected') && (
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

            {restaurantState.matches('loadMenuItemsResolved') && (
                <>
                    <Modal maskClosable={false} footer={null} visible={true} onCancel={() =>
                        send({type: 'CANCEL_DETAIL'})}
                    >
                        <p className={"center_text"}>These are our menu options for {restaurantState.context.menuOptions && restaurantState.context.menuOptions.length > 0
                                ? restaurantState.context.menuOptions[0].restaurantName
                                : ''}
                        </p>
                        <Table rowKey="id" dataSource={restaurantState.context.menuOptions} columns={columnsDetail}/>
                        {(restaurantState.context.currentOrder &&
                            restaurantState.context.currentOrder.orderLines &&
                            restaurantState.context.currentOrder.orderLines.length > 0) &&
                            <>
                                <Button>Save Order</Button>
                                <Button>Send Order</Button>
                            </>
                        }
                    </Modal>
                </>
            )}

            {restaurantState.matches('pickOrder') && (
                <>
                    {pickVisible && <PickQuantity
                        key={restaurantState.context.currentOrder.id ? restaurantState.context.currentOrder.id : 0}
                        order={restaurantState.context.currentOrder}
                        product={currentProduct.current}
                        visible={pickVisible}
                        onOk={(order: Order) => {
                            send({type: 'SAVE', payload: {order: order}})
                            setPickVisible(false)
                            console.log("order ->",order)
                        }}
                        onCancel={() => {
                            setPickVisible(false)
                            send({type: 'CANCEL_PICK'})
                            currentProduct.current = {} as MenuRestaurant
                        }}
                    />}
                </>
            )}

            {restaurantState.matches('loadMenuItemsRejected') && (
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
        </>
    )

}

export default Restaurants

interface RestaurantMachineContext {
    restaurant: Restaurant
    restaurants: Array<Restaurant>
    menuOptions: Array<MenuRestaurant>
    currentOrder?: Order
}

interface RestaurantMachineSchema {
    context: RestaurantMachineContext
    states: {
        loadingRestaurantData: {}
        loadRestaurantDataResolved: {}
        loadRestaurantDataRejected: {}
        loadingMenuData: {}
        loadMenuItemsResolved: {}
        loadMenuItemsRejected: {}
        deletingRestaurantData: {}
        pickOrder: {}
        saveOrder: {}
        submitOrder: {}
    }
}

type RestaurantMachineEvent = | { type: 'RETRY' }
    | { type: 'CANCEL' }
    | { type: 'CANCEL_DETAIL' }
    | { type: 'CANCEL_PICK' }
    | { type: 'DETAIL'; payload: { restaurant: Restaurant } }
    | { type: 'DELETE'; payload: { restaurantId: number } }
    | { type: 'PICK'; payload: { restaurantId: number } }
    | { type: 'SAVE'; payload: { order: Order } }
    | { type: 'SUBMIT_ORDER'; payload: { order: Order } }

const createRestaurantMachine = (userContext: UserContextInterface | null,) => Machine<RestaurantMachineContext, RestaurantMachineSchema, RestaurantMachineEvent>(
    {
        id: 'restaurant-machine',
        context: {
            restaurant: {} as Restaurant,
            restaurants: [],
            menuOptions: []
        },
        initial: 'loadingRestaurantData',
        on: {
            RETRY: 'loadingRestaurantData'
        },
        states: {
            loadingRestaurantData: {
                invoke: {
                    id: 'loadingRestaurantData',
                    src: 'loadRestaurantData',
                    onDone: {
                        target: 'loadRestaurantDataResolved',
                        actions: assign((context, event) => {
                            return {
                                ...context,
                                restaurants: event.data.data
                            }
                        })
                    },
                    onError: {
                        target: 'loadRestaurantDataRejected'
                    }
                }
            },
            loadRestaurantDataResolved: {
                entry: 'cleanRestaurant',
                on: {
                    RETRY: {
                        target: 'loadingRestaurantData'
                    },
                    DETAIL: {
                        target: 'loadingMenuData'
                    },
                    DELETE: {
                        target: 'deletingRestaurantData'
                    }
                }
            },
            loadRestaurantDataRejected: {
                on: {
                    RETRY: {
                        target: 'loadingRestaurantData'
                    }
                }
            },
            loadingMenuData: {
                invoke: {
                    src: 'loadMenuData',
                    onDone: {
                        target: 'loadMenuItemsResolved',
                        actions: assign((context, event) => {
                            return {
                                ...context,
                                menuOptions: event.data[0].data,
                                currentOrder: event.data[1].data.length > 0 ? event.data[1].data[0] : {} as Order
                            }
                        })
                    },
                    onError: {
                        target: 'loadMenuItemsRejected'
                    }
                }
            },
            loadMenuItemsResolved: {
                on: {
                    RETRY: {
                        target: 'loadingMenuData'
                    },
                    CANCEL_DETAIL: {
                        target: 'loadRestaurantDataResolved'
                    },
                    PICK: {
                        target: 'pickOrder'
                    },
                    SUBMIT_ORDER: {
                        target: 'submitOrder'
                    }
                }
            },
            loadMenuItemsRejected: {
                on: {
                    RETRY: {
                        target: 'loadingMenuData'
                    }
                }
            },
            deletingRestaurantData: {
                invoke: {
                    id: 'deletingRestaurantData',
                    src: 'deleteRestaurantData',
                    onDone: {
                        target: 'loadingRestaurantData'
                    },
                    onError: {
                        target: 'loadingRestaurantData'
                    }
                }
            },
            pickOrder: {
                on: {
                    SAVE: {
                        target: 'saveOrder'
                    },
                    CANCEL_PICK: {
                        target: 'loadMenuItemsResolved'
                    }
                }
            },
            saveOrder: {
                entry: 'saveOrder',
                always: {target: 'loadMenuItemsResolved'}
            },
            submitOrder: {
                invoke: {
                    src: 'sendOrder',
                    onDone: {
                        target: 'loadingRestaurantData',
                        actions: assign((context, event) => {
                            return {
                                ...context,
                                menuOptions: event.data[0].data,
                                currentOrder: event.data[1].data
                            }
                        })
                    },
                    onError: {
                        target: 'loadMenuItemsResolved'
                    }
                }
            },

        }
    },
    {
        actions: {
            cleanRestaurant: assign((context, _) => {
                return {
                    ...context,
                    restaurant: {} as Restaurant
                }
            }),
            saveOrder: assign((context, event) => {
                return {
                    ...context,
                    currentOrder: event.type === 'SAVE' ? event.payload.order : context.currentOrder
                }
            })
        },
        services: {
            loadRestaurantData: () => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/restaurants/all`
                return async () => axios
                    .get(url, {headers: {"Authorization": `Bearer ${token}`, "Content-Type": "application/json"}})
                    .then((ret) => Promise.resolve(ret)
                    )
                    .catch((err) => {
                        return Promise.reject(err)
                    })
            },
            loadMenuData: (id, event) => {
                if (event.type === 'DETAIL') {
                    const token = userContext ? userContext.accessToken : ''
                    const url = `http://${process.env.REACT_APP_SERVER_NAME}/menuRestaurant/${event.payload.restaurant.id}`
                    return Promise.all([axios.get(url, {
                        headers: {
                            "Authorization": `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    }), axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/orders/new`,
                        {
                            headers: {
                                "Authorization": `Bearer ${token}`,
                                "Content-Type": "application/json"
                            }
                        })])
                } else
                    return Promise.resolve(() => 'error')
            },
            deleteRestaurantData: (id, event) => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/restaurants/delete/${event.type === 'DELETE' ? event.payload.restaurantId : 0}`
                return async () => axios
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
