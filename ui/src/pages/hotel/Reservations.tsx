import {Button, Col, Modal, Row, Table} from "antd";
import axios from "axios";
import React, {useCallback, useEffect, useRef, useState} from "react";
import {UserContext, UserContextInterface} from "../../App";
import {Reservation} from "../../model/Reservation";
import {User} from "@auth0/auth0-spa-js";
import {displayNotification} from "../../shared/displayNotification";
import {QuestionCircleOutlined} from "@ant-design/icons";

const Reservations: React.VFC = () => {
    const value: UserContextInterface | null = React.useContext(UserContext)
    const [reservations, setReservations] = useState<any>(null)
    const [submit, setSubmit] = useState(0)
    const [dialog, setDialog] = useState(false)
    const currentReservation = useRef(0)

    const okDialog = (id:number) : void  => {
        cancelReservation();
        setSubmit(1)
        displayNotification('Info','Saving has been done', 1)
        setDialog(false)
        currentReservation.current = 0
    }

    const cancelDialog = () : void => {
        setDialog(false)
        currentReservation.current = 0
    }

    const cancelReservation = useCallback(() => {
        const token = value ? value.accessToken : ''
        const url = `http://${process.env.REACT_APP_SERVER_NAME}/reservations/reservation/${currentReservation.current}/cancel`
        return axios.post(url, {}, {headers: {"Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"} })
    }, [currentReservation, value]);

    // const cancelReservation = (reservationId:number) => {
    //     const token = value ? value.accessToken : ''
    //     const url = `http://${process.env.REACT_APP_SERVER_NAME}/reservations/reservation/${reservationId}/cancel`
    //     return axios.post(url, {}, {headers: {"Authorization": `Bearer ${token}`,
    //             "Content-Type": "application/json"} })
    // }
    
    useEffect(() => {
        const textUrl = `http://${process.env.REACT_APP_SERVER_NAME}/reservations/all`

        const getText = async () => {
            if (value?.accessToken){
                const result = await axios.get(textUrl, {headers: {
                        "Authorization": `Bearer ${value?.accessToken}`,
                        "Content-Type": "application/json"
                    }})
                    .then(response => response)
                    .catch(err => {
                        console.error(err)
                        return err
                    })
                setReservations(result.data)
            }
        };
        getText()
        setSubmit(0)
    },[submit, value?.accessToken, cancelReservation]);

    const getSubFromUser = (user:User|undefined) => {
        if (user){
            const value = user && user.sub ? user.sub : ''
            return value.substring(value.indexOf("|") + 1,value.length- 1);
        }
        return ''
    }

    function formatDate(value?: string) {
        if (value === undefined || value === null){
            return ''
        }
        return value.slice(0,10)
    }

    const columns = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
            width: '15%',
            align: 'right' as 'right'
        },
        {
            title: 'Hotel Name',
            dataIndex: 'hotelName',
            key: 'hotelName',
        },
        {
            title: 'Arriving Date',
            dataIndex: 'dateIn',
            render:(value:string)=><span>{formatDate(value)}</span>,
            key: 'dataIn',
        },
        {
            title: 'Departure Date',
            dataIndex: 'dateOut',
            render:(value:string)=><span>{formatDate(value)}</span>,
            key: 'dataOut',
        },
        {
            title: 'Cancel',
            key: 'cancel',
            hidden: value?.role.toString() === "ADMIN",
            render: (record: Reservation) => (
                record.valid === 1 ?  <Button onClick={() => {
                    currentReservation.current = record.id
                    setDialog(true)
                }
                }> Cancel </Button> : <> </>

            )
        }
    ].filter(item => !item.hidden);

    return (
        <>
         <Table rowKey="id" dataSource={ reservations && reservations.length>0 ?
             reservations.filter((r:Reservation)=>r.userId=getSubFromUser(value?.user)) : []} columns={columns}/>
        <Modal title={<Row className="inline-block align-text-bottom">
                           <Col>
                               <QuestionCircleOutlined style={{ color: 'red' }} />
                           </Col>
                           <Col>
                               <p>Warning</p>
                           </Col>
                       </Row>} visible={dialog} onOk={()=>okDialog(currentReservation.current)} onCancel={cancelDialog} maskClosable={false}>
           <span className="align-middle">
               <p>Are you sure you want to cancel the reservation ?</p>
           </span>
        </Modal>
        </>

    )      
}    

export default Reservations