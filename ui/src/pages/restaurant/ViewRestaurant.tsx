import React from "react";
import {useMachine} from "@xstate/react";
import {Button, Form, Image, Modal, Result, Spin} from "antd";
import {assign, Machine} from "xstate";
import axios from "axios";
import {Restaurant} from "../../model/Restaurant";
import {UserContext, UserContextInterface} from "../../App";

interface ViewRestaurantProps {
    restaurantId: number
    visible: boolean
    onCancel: () => void
}

const ViewRestaurant: React.FC<ViewRestaurantProps> = ({restaurantId, visible,onCancel}) => {
    const value: UserContextInterface | null = React.useContext(UserContext)

    const [restaurantState, send] = useMachine(
        createRestaurantViewMachine(
            value,
            restaurantId
        )
    )

    return (
        <>
            {restaurantState.matches('loadingRestaurant') && (
                <div>
                    <Spin/>
                </div>
            )}

            {restaurantState.matches('loadRestaurantDone') && (
                <div>
                    <Modal maskClosable={false} visible={visible} footer={null} onCancel={onCancel}>
                        <Form>
                            <Form.Item label="Name" style={{width: '100%', justifyContent: 'center'}}>
                                    {restaurantState.context.restaurant.name}
                            </Form.Item>
                            <Form.Item style={{width: '100%', justifyContent: 'center'}}>
                                <Image
                                    width="480px"
                                    src={process.env.PUBLIC_URL + '/' + restaurantState.context.restaurant.image}
                                    alt={restaurantState.context.restaurant.image}
                                    preview={false}
                                />
                            </Form.Item>

                        </Form>
                    </Modal>
                </div>
            )}

            {restaurantState.matches('loadRestaurantRejected') && (
                <div>
                    <Modal maskClosable={false} visible={visible} onCancel={() => onCancel()} footer={null}>
                        <Result
                            status="error"
                            title="Loading failed"
                            extra={<Button size="large" type="primary" onClick={() => {
                                send({
                                    type: 'RETRY'
                                })
                            }}>Try Again</Button>}
                        />
                    </Modal>
                </div>
            )}
        </>
    )

}

export default ViewRestaurant

interface ViewRestaurantMachineContext {
    restaurant: Restaurant
}

interface ViewRestaurantMachineSchema {
    context: ViewRestaurantMachineContext
    states: {
        loadingRestaurant: {}
        loadRestaurantDone: {}
        loadRestaurantRejected: {}
    }
}

type ViewRestaurantMachineEvent = | { type: 'RETRY' } | { type: 'TOOGLE' }

const createRestaurantViewMachine = (userContext: UserContextInterface | null, restaurantId: number) =>
    Machine<ViewRestaurantMachineContext, ViewRestaurantMachineSchema, ViewRestaurantMachineEvent>(
        {
            id: 'view-restaurant-machine',
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
                            target: 'loadRestaurantDone',
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
                loadRestaurantDone: {
                    on: {
                        RETRY: {
                            target: 'loadingRestaurant'
                        }
                    }
                },
                loadRestaurantRejected: {
                    on: {
                        RETRY: {
                            target: 'loadingRestaurant'
                        }
                    }
                }
            }
        },
        {
            services: {
                loadRestaurant: () => getRestaurantById(restaurantId, userContext)
            }
        }
    )

function getRestaurantById(id: number, userContext: UserContextInterface | null): Promise<Restaurant | string> {
    if (id === undefined || id === null) {
        return Promise.reject("some error")
    } else if (id === 0) {
        const restaurant = {
            id: 0,
            name: '',
            image: ''
        } as Restaurant
        return Promise.resolve(restaurant)
    } else {
        const token = userContext ? userContext.accessToken : ''
        return axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/restaurants/${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        })
    }
}