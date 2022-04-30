import React from "react";
import {useMachine} from "@xstate/react";
import {Button, Form, Input, message, Modal, Result, Spin,} from "antd";
import {assign, Machine} from "xstate";
import axios from "axios";
import {Hotel} from "../../model/Hotel";
import {UserContext, UserContextInterface} from "../../App";
import {displayNotification} from "../../shared/displayNotification";

const onOk = () => {
    message.success('Saving done', 2)
}

const onError = () => {
    message.error('Error at save', 2)
}

const onFinish = (values: any) => {
    console.log('Success:', values);
};

const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
};

interface AddEditHotelProps {
    hotelId: number
    visible: boolean
    onSubmit: () => void
    onCancel: () => void
    onRefresh: () => void
}

const AddEditHotel: React.FC<AddEditHotelProps> = ({hotelId, visible, onSubmit, onCancel, onRefresh}) => {

    const value:UserContextInterface|null = React.useContext(UserContext)

    const [form] = Form.useForm()

    const [hotelState, send] = useMachine(
        createHotelMachine(
            value,
            hotelId,
            onOk,
            onError,
            onSubmit,
            onCancel,
            onRefresh
        )
    )

    const titleModal = () => {
        return hotelId === 0 ? 'Hotel details' : 'Hotel details for ' + hotelState.context.hotel.id
    }

    return (
        <>
            {hotelState.matches('loadingHotel') && (
                <>
                    <Spin/>
                </>
            )}

            {hotelState.matches('loadHotelResolved') && (
                <>
                    <div>
                        <Modal maskClosable={false} title={titleModal()}
                               visible={visible}
                               onOk={() => {
                                   send({
                                           type: 'SAVE',
                                           payload: {hotel: hotelState.context.hotel}
                                       }
                                   )
                                   onSubmit()
                                   displayNotification('Info','Saving has been done', 1)
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
                                    name: hotelState.context.hotel.name,
                                    zone: hotelState.context.hotel.zone,
                                    nr_rooms: hotelState.context.hotel.nr_rooms,
                                    image: hotelState.context.hotel.image
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
                                        hotelState.context.hotel.name = e.target.value
                                    }}/>
                                </Form.Item>

                                <Form.Item
                                    label="Zone"
                                    name="zone"
                                    rules={[{required: true, message: 'Please input zone'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        hotelState.context.hotel.zone = e.target.value
                                    }}/>
                                </Form.Item>

                                <Form.Item
                                    label="Number of rooms"
                                    name="nr_rooms"
                                    rules={[{required: true, message: 'Please input number of rooms'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        hotelState.context.hotel.nr_rooms = Number(e.target.value)
                                    }}/>
                                </Form.Item>

                                <Form.Item
                                    label="Image name"
                                    name="image"
                                    rules={[{required: true, message: 'Please input image name'}]}
                                >
                                    <Input onChange={(e) => {
                                        e.preventDefault()
                                        hotelState.context.hotel.image = e.target.value
                                    }}/>
                                </Form.Item>
                            </Form>

                        </Modal>
                    </div>
                </>
            )}

            {hotelState.matches('loadHotelRejected') && (
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

export default AddEditHotel

interface AddEditHotelMachineContext {
    hotel: Hotel
}

interface AddEditHotelMachineSchema {
    context: AddEditHotelMachineContext
    states: {
        loadingHotel: {}
        loadHotelResolved: {}
        loadHotelRejected: {}
        savingHotel: {}
    }
}

type AddEditHotelMachineEvent = | { type: 'RETRY' } | { type: 'SAVE'; payload: { hotel: Hotel } }

const createHotelMachine = (userContext:UserContextInterface|null,
                            hotelId: number,
                            onOk: () => void,
                            onError: () => void,
                            onSubmit: () => void,
                            onCancel: () => void,
                            onRefresh: () => void) =>
    Machine<AddEditHotelMachineContext, AddEditHotelMachineSchema, AddEditHotelMachineEvent>(
        {
            id: 'addedit-hotel-machine',
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
                            target: 'loadHotelResolved',
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
                loadHotelResolved: {
                    on: {
                        RETRY: {
                            target: 'loadingHotel'
                        },
                        SAVE: {
                            target: 'savingHotel'
                        }
                    }
                },
                loadHotelRejected: {
                    on: {
                        RETRY: {
                            target: 'loadingHotel'
                        }
                    }
                },
                savingHotel: {
                    invoke: {
                        id: 'savingHotel',
                        src: 'saveHotel',
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
                loadHotel: () => getHotelById(hotelId, userContext),
                saveHotel: (id, event) => {
                    if (event.type === 'SAVE'){
                        const token = userContext ? userContext.accessToken : ''
                        const url = `http://${process.env.REACT_APP_SERVER_NAME}/hotels/hotel`
                        return axios.post(url, event.payload.hotel, {headers: {"Authorization": `Bearer ${token}`,
                                                                                     "Content-Type": "application/json"} })
                    }
                    else
                        return Promise.resolve(() => 'error')
                }
            }
        }
    )

function getHotelById(id: number,userContext: UserContextInterface | null): Promise<Hotel | string> {
    if (id === undefined || id === null) {
        return Promise.reject("some error")
    } else if (id === 0) {
        const hotel = {
            id: 0,
            name: '',
            zone: '',
            nr_rooms: 0
        }
        return Promise.resolve(hotel)
    } else{
        const token = userContext ? userContext.accessToken : ''
        return axios.get(`http://${process.env.REACT_APP_SERVER_NAME}/hotels/${id}`,{headers: {"Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"} })
    }

}