import {OrderLine} from "./OrderLine";

export interface Order {
    id: number,
    restaurantName: string,
    status: string,
    price: number,
    orderLines: OrderLine[],
    userId?: string
}