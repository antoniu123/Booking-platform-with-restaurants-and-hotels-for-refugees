import {Table} from "antd";
import axios from "axios";
import React, {useEffect, useState} from "react";
import {UserContext, UserContextInterface} from "../App";
import {User} from "@auth0/auth0-spa-js";
import {Order} from "../model/Order";

const Orders: React.VFC = () => {
    const value: UserContextInterface | null = React.useContext(UserContext)
    const [orders, setOrders] = useState<any>(null)

    
    useEffect(() => {
        const textUrl = `http://${process.env.REACT_APP_SERVER_NAME}/orders/completed`
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
                setOrders(result.data)
            }
        };

        getText()
    },[value?.accessToken]);

    const getSubFromUser = (user:User|undefined) => {
        if (user){
            const value = user && user.sub ? user.sub : ''
            return value.substring(value.indexOf("|") + 1,value.length- 1);
        }
        return ''
    }

    const columns = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Restaurant Name',
            dataIndex: 'restaurantName',
            key: 'restaurantName',
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
    ]

    const columnDetails = [
        {
            title: 'Id',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Food',
            dataIndex: 'menuRestaurantName',
            key: 'menuRestaurantName',
        },
        {
            title: 'Qty',
            dataIndex: 'quantity',
            key: 'quantity',
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
        },
    ]

    return (
        <>

            <div className="site-card-wrapper">
                <Table rowKey="id" scroll={{x: 'calc(1200px+50%)'}} bordered
                       dataSource={orders && orders.length>0 ?
                           orders.filter((o:Order)=>o.userId=getSubFromUser(value?.user)) : []}
                       expandable={{
                           expandedRowRender: (record:Order) =>
                               <>
                                   <div>Order details</div>
                                   <div style={{margin: 10}}>
                                       <Table rowKey="id" dataSource={record.orderLines} pagination={false}
                                              bordered={true}
                                              columns={columnDetails} size="small"/>
                                   </div>
                               </>,
                           rowExpandable: record => record.orderLines.length > 0,
                       }}
                       rowClassName={(record, index) => {
                           return index % 2 ? 'shallow_gray' : 'deep_gray'
                       }}
                       columns={columns} size="small"/>
            </div>
        </>
    )      
}    

export default Orders