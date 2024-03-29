import React, {useState} from "react";
import {assign, Machine} from "xstate";
import axios from "axios";
import {useMachine} from "@xstate/react";
import {Alert, Button, DatePicker, Form, Modal, Result, Spin, Table} from 'antd';
import {Hotel} from "../../model/Hotel";
import AddEditHotel from "./AddEditHotel";
import ViewHotel from "./ViewHotel";
import {UserContext, UserContextInterface} from "../../App";
import {Reservation} from "../../model/Reservation";
import moment from "moment";
import {displayNotification} from "../../shared/displayNotification";

function disabledDate(current:any) {
    return current && current < moment().endOf('day');
}

const Hotels: React.FC = () => {

    const value: UserContextInterface | null = React.useContext(UserContext)
    const [hotelState, send] = useMachine(createHotelMachine(value))
    const [hotelId, setHotelId] = useState(0);
    const [hotelName, setHotelName] = useState('');
    const [pickVisible, setPickVisible] = useState(false)
    const [addEditVisible, setAddEditVisible] = useState(false);
    const [detailVisible, setDetailVisible] = useState(false);
    const [formSave] = Form.useForm();
    const submit = () => {
        if (formSave.getFieldValue('dateIn') === undefined ||
            formSave.getFieldValue('dateOut') === undefined){
            displayNotification('Error', "Please fill the dates",
                2);
            return
        }
        if (formSave.getFieldValue('dateIn').format('YYYY-MM-DD') >=
            formSave.getFieldValue('dateOut').format('YYYY-MM-DD')){
            displayNotification('Error', "Departure date must be greater than arrive date",
                2);
            return
        }
        send({
            type: 'SAVE',
            payload: {reservation: {...hotelState.context.reservation,
                                    hotelName: hotelName,
                                    dateIn: formSave.getFieldValue("dateIn").utc(true).format('YYYY-MM-DD'),
                                    dateOut: formSave.getFieldValue("dateOut").utc(true).format('YYYY-MM-DD'),
                                    valid: 1
            }}
        })
        setHotelName('')
    }

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
            title: 'Hotel',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Zone',
            dataIndex: 'zone',
            key: 'zone',
        },
        {
            title: 'Number of rooms',
            dataIndex: 'nr_rooms',
            key: 'nrRooms',
        },
        {
            title: 'Edit',
            key: 'edit',
            hidden: value?.role.toString() !== "ADMIN",
            render: (record: Hotel) => (
                <Button hidden={value?.role.toString() !== "ADMIN"} type="primary" ghost onClick={
                    () => {
                        setHotelId(record.id)
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
                            type: 'DELETE', payload: {hotelId: record.id}
                        })
                    }
                }> Delete </Button>
            )
        },
        {
            title: 'Detail',
            key: 'detail',
            hidden: value?.role.toString() !== "USER",
            render: (record: Hotel) => (
                <Button hidden={value?.role.toString() !== "USER"} onClick={() => {
                    setHotelId(record.id)
                    setDetailVisible(true)
                }
                }> Details </Button>
            )
        },
        {
            title: 'Pick',
            key: 'pick',
            hidden: value?.role.toString() !== "USER",
            render: (record: Hotel) => (
                <Button hidden={value?.role.toString() !== "USER"} onClick={() => {
                    setHotelId(record.id)
                    setHotelName(record.name)
                    setPickVisible(true)
                    send("PICK");
                }
                }> Pick </Button>
            )
        }
    ].filter(item => !item.hidden);

    return (
        <div>
            {(hotelState.matches('loadingHotelData') ||
              hotelState.matches('savingReservation') ||
                hotelState.matches('deletingHotelData')) && (
                <>
                    <Spin>
                        <Alert message="Please wait for loading" type="info"/>
                    </Spin>
                </>
            )}

            {hotelState.matches('loadHotelDataResolved') && (
                <>
                    <p className={"center_text"}>These are our hotel options</p>
                    {value?.role.toString() === "ADMIN" &&
                        <Button type="primary" onClick={
                            () => {
                                setHotelId(0)
                                setAddEditVisible(true)
                            }
                        }>Add
                        </Button>
                    }
                    <Table rowKey="id" dataSource={hotelState.context.hotels} columns={columns}/>
                    <AddEditHotel key={hotelId}
                                  hotelId={hotelId}
                                  visible={addEditVisible}
                                  onSubmit={() => setAddEditVisible(false)}
                                  onCancel={() => setAddEditVisible(false)}
                                  onRefresh={() => refresh()}
                    />
                    <ViewHotel key={hotelId}
                               hotelId={hotelId}
                               visible={detailVisible}
                               onCancel={() => setDetailVisible(false)}
                    />
                </>
            )}

            {hotelState.matches('loadHotelDataRejected') && (
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

            {hotelState.matches('pickingHotel') && (
                <Modal maskClosable={false} title={"Pick date for reservation"}
                       visible={pickVisible}
                       onOk={submit}
                       onCancel={()=>{
                           send({type:'CANCEL'})
                           setPickVisible(false)}
                       }
                       width={800}

                >
                    <Form form={formSave}
                        name="basic"
                        labelCol={{span: 8}}
                        wrapperCol={{span: 16}}
                        autoComplete="off"
                        initialValues={{
                        dateIn: hotelState.context.reservation.dateIn? hotelState.context.reservation.dateIn : undefined,
                        dateOut: hotelState.context.reservation.dateOut? hotelState.context.reservation.dateOut : undefined
                    }}
                    >
                        <Form.Item
                            key={hotelState.context.reservation.dateIn? hotelState.context.reservation.dateIn : 1}
                            name="dateIn"
                            label="Arriving Date"
                            rules={[{ required: true, message: "Please select arriving date!"}]}
                        >
                            <DatePicker format="YYYY-MM-DD" disabledDate={disabledDate}/>
                        </Form.Item>
                        <Form.Item
                            key={hotelState.context.reservation.dateOut? hotelState.context.reservation.dateOut : 2}
                            name="dateOut"
                            label="Departure Date"
                            rules={[{ required: true, message: "Please select departure date!"},
                            ({ getFieldValue }) => ({
                                validator(_, value) {
                                    if (!value ||
                                        getFieldValue('dateIn').format('YYYY-MM-DD') < value.format('YYYY-MM-DD')) {
                                        return Promise.resolve();
                                    }
                                    return Promise.reject(new Error('Arriving date must be before departure date!'));
                                }
                            })
                            ]}

                        >
                            <DatePicker format="YYYY-MM-DD" disabledDate={disabledDate}/>
                        </Form.Item>

                    </Form>
                </Modal>
            )}

        </div>
    )

}

export default Hotels

interface HotelMachineContext {
    hotels: Array<Hotel>
    reservation: Reservation
}

interface HotelMachineSchema {
    context: HotelMachineContext
    states: {
        loadingHotelData: {}
        loadHotelDataResolved: {}
        loadHotelDataRejected: {}
        deletingHotelData: {}
        pickingHotel: {}
        savingReservation: {}
    }
}

type HotelMachineEvent = | { type: 'RETRY' }
    | { type: 'CANCEL' }
    | { type: 'DELETE'; payload: { hotelId: number } }
    | { type: 'PICK' }
    | { type: 'SAVE'; payload: { reservation: Reservation } }

const createHotelMachine = (userContext: UserContextInterface | null) => Machine<HotelMachineContext, HotelMachineSchema, HotelMachineEvent>(
    {
        id: 'hotel-machine',
        context: {
            hotels: [],
            reservation: {} as Reservation
        },
        initial: 'loadingHotelData',
        on: {
            RETRY: 'loadingHotelData'
        },
        states: {
            loadingHotelData: {
                invoke: {
                    id: 'loadingHotelData',
                    src: 'loadHotelData',
                    onDone: {
                        target: 'loadHotelDataResolved',
                        actions: assign((context, event) => {
                            return {
                                hotels: event.data.data
                            }
                        })
                    },
                    onError: {
                        target: 'loadHotelDataRejected'
                    }
                }
            },
            loadHotelDataResolved: {
                on: {
                    RETRY: {
                        target: 'loadingHotelData'
                    },
                    DELETE: {
                        target: 'deletingHotelData'
                    },
                    PICK: {
                        target: 'pickingHotel'
                    }
                }
            },
            loadHotelDataRejected: {
                on: {
                    RETRY: {
                        target: 'loadingHotelData'
                    }
                }
            },
            deletingHotelData: {
                invoke: {
                    id: 'deletingHotelData',
                    src: 'deleteHotelData',
                    onDone: {
                        target: 'loadingHotelData',
                        actions: 'ok'
                    },
                    onError: {
                        target: 'loadingHotelData',
                        actions: 'error'
                    }
                }
            },
            pickingHotel: {
                on: {
                    SAVE: {
                        target: 'savingReservation'
                    },
                    CANCEL: {
                        target: 'loadHotelDataResolved'
                    }
                }
            },
            savingReservation: {
                invoke: {
                    id: 'savingReservation',
                    src: 'saveReservation',
                    onDone: {
                        target: 'loadingHotelData',
                        actions: 'ok'
                    },
                    onError: {
                        target: 'pickingHotel',
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
            loadHotelData: () => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/hotels/all`
                return async () => axios
                    .get(url, {headers: {"Authorization": `Bearer ${token}`, "Content-Type": "application/json"}})
                    .then((ret) => Promise.resolve(ret)
                    )
                    .catch((err) => {
                        return Promise.reject(err)
                    })
            },
            deleteHotelData: (id, event) => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/hotels/delete/${event.type === 'DELETE' ? event.payload.hotelId : 0}`
                return async () => axios
                    .delete(url, {headers: {"Authorization": `Bearer ${token}`, "Content-Type": "application/json"}})
                    .then((ret) => Promise.resolve(ret)
                    )
                    .catch((err) => {
                        return Promise.reject(err)
                    })
            },
            saveReservation: (id, event) => {
                const token = userContext ? userContext.accessToken : ''
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/reservations/reservation`
                const body = event.type === 'SAVE' ? event.payload.reservation : {} as Reservation
                return async () => axios
                    .post(url, body, {
                        headers: {
                            "Authorization": `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    })
                    .then((ret) => Promise.resolve(ret)
                    )
                    .catch((err) => {
                        return Promise.reject(err)
                    })
            }
        }
    }
)
