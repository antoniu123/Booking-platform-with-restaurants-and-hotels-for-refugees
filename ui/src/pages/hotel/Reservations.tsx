import {Button, Table} from "antd";
import axios from "axios";
import React, {useEffect, useState} from "react";
import {UserContext, UserContextInterface} from "../../App";
import {Reservation} from "../../model/Reservation";
import {User} from "@auth0/auth0-spa-js";
import {displayNotification} from "../../shared/displayNotification";

const Reservations: React.VFC = () => {
    const value: UserContextInterface | null = React.useContext(UserContext)
    const [reservations, setReservations] = useState<any>(null)
    const [submit, setSubmit] =useState(0)
    
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
    },[submit, value?.accessToken]);

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

    const cancelReservation = (reservationId:number) => {
        const token = value ? value.accessToken : ''
        const url = `http://${process.env.REACT_APP_SERVER_NAME}/reservations/reservation/${reservationId}/cancel`
        return axios.post(url, {}, {headers: {"Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"} })
    }

    const columns = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
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
            render: (record: Reservation) => (
                record.valid === 1 ?  <Button onClick={() => {
                    cancelReservation(record.id);
                    setSubmit(1)
                    displayNotification('Info','Saving has been done', 1)

                }
                }> Cancel </Button> : <> </>

            )
        }
    ]

    return (
        <>
         <Table rowKey="id" dataSource={ reservations && reservations.length>0 ?
             reservations.filter((r:Reservation)=>r.userId=getSubFromUser(value?.user)) : []} columns={columns}/>
        </>  
    )      
}    

export default Reservations