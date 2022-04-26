import {OrderLine} from "./OrderLine";

export interface Order{
    id: number,
    restaurantName: string,
    status: string,
    price: number,
    orderLineDtoList: OrderLine[],
    userId: string
}