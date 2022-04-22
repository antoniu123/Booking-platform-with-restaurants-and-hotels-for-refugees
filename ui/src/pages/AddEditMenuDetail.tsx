import React from "react";
import {useMachine} from "@xstate/react";
import {Button, Form, Input, message, Modal, Result, Select, Spin,} from "antd";
import {assign, Machine} from "xstate";
import axios from "axios";
import {MenuRestaurant} from "../model/MenuRestaurant";
import {UserContext, UserContextInterface} from "../App";
import {Restaurant} from "../model/Restaurant";

const { Option } = Select;

const onOk = () => {
    message.success('saving done', 2)
}

const onError = () => {
    message.error('error at save', 2)
}

const onFinish = (values: any) => {
    console.log('Success:', values);
};

const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
};

interface AddEditMenuRestaurantProps {
    menuRestaurantId: number
    visible: boolean
    onSubmit: () => void
    onCancel: () => void
    onRefresh: () => void
}

const AddEditMenuDetail: React.FC<AddEditMenuRestaurantProps> = ({menuRestaurantId, visible, onSubmit, onCancel, onRefresh}) => {

    const value:UserContextInterface|null = React.useContext(UserContext)

    const images:string[] = ["beef.jpeg","pork_ribs.jpg"]

    const [form] = Form.useForm()

    const [menuRestaurantState, send] = useMachine(
        createMenuRestaurantMachine(
            value,
            menuRestaurantId,
            onOk,
            onError,
            onSubmit,
            onCancel,
            onRefresh
        )
    )

    const titleModal = () => {
        return menuRestaurantId === 0 ? 'MenuRestaurant details' : 'MenuRestaurant details for ' + menuRestaurantState.context.menuRestaurant.id
    }

    return (
        <>
            {menuRestaurantState.matches('loadingMenuRestaurant') && (
                <>
                    <Spin/>
                </>
            )}

            {menuRestaurantState.matches('loadMenuRestaurantResolved') && (
                <>
                    <div>
                        <Modal maskClosable={false} title={titleModal()}
                               visible={visible}
                               onOk={() => {
                                   send({
                                           type: 'SAVE',
                                           payload: {menuRestaurant: {
                                               ...menuRestaurantState.context.menuRestaurant,
                                               restaurantName: form.getFieldValue('restaurantName'),
                                               image: form.getFieldValue('image')
                                              }
                                           }
                                       }
                                   )
                                   onSubmit()
                               }
                               }
                               onCancel={onCancel}

                        >
                            <Form
                                name="basic"
                                form={form}
                                labelCol={{span: 8}}
                                wrapperCol={{span: 16}}
                                initialValues={{
                                    restaurantName: menuRestaurantState.context.menuRestaurant.restaurantName,
                                    name: menuRestaurantState.context.menuRestaurant.name,
                                    price: menuRestaurantState.context.menuRestaurant.price,
                                    image: menuRestaurantState.context.menuRestaurant.image
                                }}
                                onFinish={onFinish}
                                onFinishFailed={onFinishFailed}
                                autoComplete="off"
                            >
                                <Form.Item
                                    label="Restaurant name"
                                    name="restaurantName"
                                    rules={[{required: true, message: 'Please input restaurant name'}]}
                                >
                                    <Select
                                    >
                                        {menuRestaurantState.context.restaurantNames.map((name:string, index:number) => {
                                            return (
                                                <Option key={index} value={name}>
                                                    {name}
                                                </Option>
                                            );
                                        })}
                                    </Select>
                                </Form.Item>

                                <Form.Item
                                    label="Name"
                                    name="name"
                                    rules={[{required: true, message: 'Please input food name'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        menuRestaurantState.context.menuRestaurant.name = e.target.value
                                    }}/>
                                </Form.Item>

                                <Form.Item
                                    label="Price"
                                    name="price"
                                    rules={[{required: true, message: 'Please input price'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        menuRestaurantState.context.menuRestaurant.price = Number(e.target.value)
                                    }}/>
                                </Form.Item>

                                <Form.Item
                                    label="Image name"
                                    name="image"
                                    rules={[{required: true, message: 'Please input image name'}]}
                                >
                                    <Select
                                    >
                                        {images.map((name:string, index:number) => {
                                            return (
                                                <Option key={index} value={name}>
                                                    {name}
                                                </Option>
                                            );
                                        })}
                                    </Select>
                                </Form.Item>
                            </Form>

                        </Modal>
                    </div>
                </>
            )}

            {menuRestaurantState.matches('loadMenuRestaurantRejected') && (
                <>
                    <Result
                        status="error"
                        title="Loading failed"
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

export default AddEditMenuDetail

interface AddEditMenuRestaurantMachineContext {
    menuRestaurant: MenuRestaurant
    restaurantNames: string[]
}

interface AddEditMenuRestaurantMachineSchema {
    context: AddEditMenuRestaurantMachineContext
    states: {
        loadingMenuRestaurant: {}
        loadMenuRestaurantResolved: {}
        loadMenuRestaurantRejected: {}
        savingMenuRestaurant: {}
    }
}

type AddEditMenuRestaurantMachineEvent = | { type: 'RETRY' } | { type: 'SAVE'; payload: { menuRestaurant: MenuRestaurant } }

const createMenuRestaurantMachine = (userContext:UserContextInterface|null,
                            menuRestaurantId: number,
                            onOk: () => void,
                            onError: () => void,
                            onSubmit: () => void,
                            onCancel: () => void,
                            onRefresh: () => void) =>
    Machine<AddEditMenuRestaurantMachineContext, AddEditMenuRestaurantMachineSchema, AddEditMenuRestaurantMachineEvent>(
        {
            id: 'addedit-menuRestaurant-machine',
            context: {
                menuRestaurant: {

                } as MenuRestaurant,
                restaurantNames: []
            },
            initial: 'loadingMenuRestaurant',
            states: {
                loadingMenuRestaurant: {
                    invoke: {
                        src: 'loadMenuRestaurant',
                        onDone: {
                            target: 'loadMenuRestaurantResolved',
                            actions: assign((context, event) => {
                                if (event.data[1].data) {
                                    if (event.data[0].data)
                                        return {
                                            menuRestaurant: event.data[0].data,
                                            restaurantNames: event.data[1].data.map((d:Restaurant)=>d.name)
                                        }
                                    else {
                                        return {
                                            menuRestaurant: event.data[0],
                                            restaurantNames: event.data[1].data.map((d:Restaurant)=>d.name)
                                        }
                                    }
                                }
                                return {
                                        menuRestaurant: {},
                                        restaurantNames:[]
                                }
                            })
                        },
                        onError: {
                            target: 'loadMenuRestaurantRejected'
                        }
                    }
                },
                loadMenuRestaurantResolved: {
                    on: {
                        RETRY: {
                            target: 'loadingMenuRestaurant'
                        },
                        SAVE: {
                            target: 'savingMenuRestaurant'
                        }
                    }
                },
                loadMenuRestaurantRejected: {
                    on: {
                        RETRY: {
                            target: 'loadingMenuRestaurant'
                        }
                    }
                },
                savingMenuRestaurant: {
                    invoke: {
                        id: 'savingMenuRestaurant',
                        src: 'saveMenuRestaurant',
                        onDone: {
                            actions: 'callOk'
                        },
                        onError: {
                            actions: 'callError'
                        }
                    }
                }
            }
        },
        {
            actions: {
                callOk: () => {
                    onOk()
                    onSubmit()
                    onRefresh()
                },
                callError: () => {
                    onError()
                    onCancel()
                    onRefresh()
                }
            },
            services: {
                loadMenuRestaurant: () => Promise.all([getMenuRestaurantById(menuRestaurantId, userContext), getRestaurants(userContext)]),
                saveMenuRestaurant: (id, event) => {
                    if (event.type === 'SAVE'){
                        const token = userContext ? userContext.accessToken : ''
                        const url = `http://${process.env.REACT_APP_SERVER_NAME}/menuRestaurant/menu`
                        return axios.post(url, event.payload.menuRestaurant, {headers: {"Authorization": `Bearer ${token}`,
                                                                                     "Content-Type": "application/json"} })
                    }
                    else
                        return Promise.resolve(() => 'error')
                }
            }
        }
    )

function getMenuRestaurantById(id: number,userContext: UserContextInterface | null): Promise<MenuRestaurant | string> {
    if (id === undefined || id === null) {
        return Promise.reject("some error")
    } else if (id === 0) {
        const menuRestaurant = {
            id: 0,
            restaurantName: '',
            name: '',
            price: 0,
            image: ''
        }
        return Promise.resolve(menuRestaurant)
    } else{
        const token = userContext ? userContext.accessToken : ''
        return axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/menuRestaurant/${id}`,{headers: {"Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"} })
    }
}

function getRestaurants(userContext: UserContextInterface | null) {
    const token = userContext ? userContext.accessToken : ''
    return axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/restaurants/all`, {
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
}