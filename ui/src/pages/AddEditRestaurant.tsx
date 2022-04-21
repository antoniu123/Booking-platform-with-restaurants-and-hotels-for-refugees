import React from "react";
import {useMachine} from "@xstate/react";
import {Button, Form, Input, message, Modal, Result, Spin,} from "antd";
import {assign, Machine} from "xstate";
import axios from "axios";
import {Restaurant} from "../model/Restaurant";
import {UserContext, UserContextInterface} from "../App";

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

interface AddEditRestarantProps {
    restaurantId: number
    visible: boolean
    onSubmit: () => void
    onCancel: () => void
    onRefresh: () => void
}

const AddEditRestaurant: React.FC<AddEditRestarantProps> = ({restaurantId, visible, onSubmit, onCancel, onRefresh}) => {

    const value:UserContextInterface|null = React.useContext(UserContext)

    const [form] = Form.useForm()

    const [restaurantState, send] = useMachine(
        createRestaurantMachine(
            value,
            restaurantId,
            onOk,
            onError,
            onSubmit,
            onCancel,
            onRefresh
        )
    )

    const titleModal = () => {
        return restaurantId === 0 ? 'Restaurant details' : 'Restaurant details for ' + restaurantState.context.restaurant.id
    }

    return (
        <>
            {restaurantState.matches('loadingRestaurant') && (
                <>
                    <Spin/>
                </>
            )}

            {restaurantState.matches('loadRestaurantResolved') && (
                <>
                    <div>
                        <Modal maskClosable={false} title={titleModal()}
                               visible={visible}
                               onOk={() => {
                                   send({
                                           type: 'SAVE',
                                           payload: {restaurant: restaurantState.context.restaurant}
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
                                    name: restaurantState.context.restaurant.name,
                                    image: restaurantState.context.restaurant.image,
                                }}
                                onFinish={onFinish}
                                onFinishFailed={onFinishFailed}
                                autoComplete="off"
                            >
                                <Form.Item
                                    label="Name"
                                    name="name"
                                    rules={[{required: true, message: 'Please input name'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        restaurantState.context.restaurant.name = e.target.value
                                    }}/>
                                </Form.Item>
                                <Form.Item
                                    label="Image name"
                                    name="image"
                                    rules={[{required: true, message: 'Please input image name'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        restaurantState.context.restaurant.image = e.target.value
                                    }}/>
                                </Form.Item>

                            </Form>

                        </Modal>
                    </div>
                </>
            )}

            {restaurantState.matches('loadRestaurantRejected') && (
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

export default AddEditRestaurant

interface AddEditRestaurantMachineContext {
    restaurant: Restaurant
}

interface AddEditRestaurantMachineSchema {
    context: AddEditRestaurantMachineContext
    states: {
        loadingRestaurant: {}
        loadRestaurantResolved: {}
        loadRestaurantRejected: {}
        savingRestaurant: {}
    }
}

type AddEditRestaurantMachineEvent = | { type: 'RETRY' } | { type: 'SAVE'; payload: { restaurant: Restaurant } }

const createRestaurantMachine = (userContext:UserContextInterface|null,
                            restaurantId: number,
                            onOk: () => void,
                            onError: () => void,
                            onSubmit: () => void,
                            onCancel: () => void,
                            onRefresh: () => void) =>
    Machine<AddEditRestaurantMachineContext, AddEditRestaurantMachineSchema, AddEditRestaurantMachineEvent>(
        {
            id: 'addedit-Restaurant-machine',
            context: {
                restaurant: {
                    id: 0,
                    name: '',
                    image: ''
                }
            },
            initial: 'loadingRestaurant',
            states: {
                loadingRestaurant: {
                    invoke: {
                        id: 'loadingRestaurant',
                        src: 'loadRestaurant',
                        onDone: {
                            target: 'loadRestaurantResolved',
                            actions: assign((context, event) => {
                                if (event.data.data)
                                    return {
                                        restaurant: event.data.data
                                    }
                                else
                                    return {
                                        restaurant: event.data
                                    }

                            })
                        },
                        onError: {
                            target: 'loadRestaurantRejected'
                        }
                    }
                },
                loadRestaurantResolved: {
                    on: {
                        RETRY: {
                            target: 'loadingRestaurant'
                        },
                        SAVE: {
                            target: 'savingRestaurant'
                        }
                    }
                },
                loadRestaurantRejected: {
                    on: {
                        RETRY: {
                            target: 'loadingRestaurant'
                        }
                    }
                },
                savingRestaurant: {
                    invoke: {
                        id: 'savingRestaurant',
                        src: 'saveRestaurant',
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
                loadRestaurant: () => getRestaurantById(restaurantId, userContext),
                saveRestaurant: (id, event) => {
                    if (event.type === 'SAVE'){
                        const token = userContext ? userContext.accessToken : ''
                        const url = `http://${process.env.REACT_APP_SERVER_NAME}/restaurants/restaurant`
                        return axios.post(url, event.payload.restaurant, {headers: {"Authorization": `Bearer ${token}`,
                                                                                     "Content-Type": "application/json"} })
                    }
                    else
                        return Promise.resolve(() => 'error')
                }
            }
        }
    )

function getRestaurantById(id: number,userContext: UserContextInterface | null): Promise<Restaurant | string> {
    if (id === undefined || id === null) {
        return Promise.reject("some error")
    } else if (id === 0) {
        const restaurant = {
            id: 0,
            name: '',
            image: ''
        }
        return Promise.resolve(restaurant)
    } else{
        const token = userContext ? userContext.accessToken : ''
        return axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/restaurants/${id}`,{headers: {"Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"} })
    }

}