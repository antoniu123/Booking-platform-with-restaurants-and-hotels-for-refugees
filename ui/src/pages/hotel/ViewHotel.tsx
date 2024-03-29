import React from "react";
import {useMachine} from "@xstate/react";
import {Button, Form, Image, Modal, Result, Spin} from "antd";
import {assign, Machine} from "xstate";
import axios from "axios";
import {Hotel} from "../../model/Hotel";
import {UserContext, UserContextInterface} from "../../App";

interface ViewHotelProps {
    hotelId: number
    visible: boolean
    onCancel: () => void
}

const ViewHotel: React.FC<ViewHotelProps> = ({hotelId, visible,onCancel}) => {
    const value: UserContextInterface | null = React.useContext(UserContext)

    const [hotelState, send] = useMachine(
        createHotelViewMachine(
            value,
            hotelId
        )
    )

    return (
        <>
            {hotelState.matches('loadingHotel') && (
                <div>
                    <Spin/>
                </div>
            )}

            {hotelState.matches('loadHotelDone') && (
                <div>
                    <Modal maskClosable={false} visible={visible} footer={null} onCancel={onCancel}>
                        <Form>
                            <Form.Item label="Name" style={{width: '100%', justifyContent: 'center'}}>
                                    {hotelState.context.hotel.name}
                            </Form.Item>
                            <Form.Item label="Zone" style={{width: '100%', justifyContent: 'center'}}>
                                    {hotelState.context.hotel.zone}
                            </Form.Item>
                            <Form.Item label="Number of rooms" style={{width: '100%', justifyContent: 'center'}}>
                                    {hotelState.context.hotel.nr_rooms}
                            </Form.Item>
                            <Form.Item style={{width: '100%', justifyContent: 'center'}}>
                                <Image
                                    width="480px"
                                    height="240px"
                                    src={process.env.PUBLIC_URL + '/' + hotelState.context.hotel.image}
                                    alt={hotelState.context.hotel.image}
                                    preview={false}
                                />
                            </Form.Item>

                        </Form>
                    </Modal>
                </div>
            )}

            {hotelState.matches('loadHotelRejected') && (
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

export default ViewHotel

interface ViewHotelMachineContext {
    hotel: Hotel
}

interface ViewHotelMachineSchema {
    context: ViewHotelMachineContext
    states: {
        loadingHotel: {}
        loadHotelDone: {}
        loadHotelRejected: {}
    }
}

type ViewHotelMachineEvent = | { type: 'RETRY' } | { type: 'TOOGLE' }

const createHotelViewMachine = (userContext: UserContextInterface | null, hotelId: number) =>
    Machine<ViewHotelMachineContext, ViewHotelMachineSchema, ViewHotelMachineEvent>(
        {
            id: 'view-hotel-machine',
            context: {
                hotel: {
                    id: 0,
                    name: '',
                    zone: '',
                    nr_rooms: 0
                }
            },
            initial: 'loadingHotel',
            states: {
                loadingHotel: {
                    invoke: {
                        id: 'loadingHotel',
                        src: 'loadHotel',
                        onDone: {
                            target: 'loadHotelDone',
                            actions: assign((context, event) => {
                                if (event.data.data)
                                    return {
                                        hotel: event.data.data
                                    }
                                else
                                    return {
                                        hotel: event.data
                                    }

                            })
                        },
                        onError: {
                            target: 'loadHotelRejected'
                        }
                    }
                },
                loadHotelDone: {
                    on: {
                        RETRY: {
                            target: 'loadingHotel'
                        }
                    }
                },
                loadHotelRejected: {
                    on: {
                        RETRY: {
                            target: 'loadingHotel'
                        }
                    }
                }
            }
        },
        {
            services: {
                loadHotel: () => getHotelById(hotelId, userContext)
            }
        }
    )

function getHotelById(id: number, userContext: UserContextInterface | null): Promise<Hotel | string> {
    if (id === undefined || id === null) {
        return Promise.reject("some error")
    } else if (id === 0) {
        const hotel = {
            id: 0,
            name: '',
            zone: '',
            nr_rooms: 0
        } as Hotel
        return Promise.resolve(hotel)
    } else {
        const token = userContext ? userContext.accessToken : ''
        return axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/hotels/${id}`, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        })
    }
}