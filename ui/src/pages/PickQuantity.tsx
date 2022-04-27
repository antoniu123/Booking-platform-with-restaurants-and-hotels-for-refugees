import {Order} from "../model/Order";
import {Card, Form, InputNumber, Modal} from "antd";
import {OrderLine} from "../model/OrderLine";
import {MenuRestaurant} from "../model/MenuRestaurant";

interface PickQuantityProps {
    order: Order
    product: MenuRestaurant
    visible: boolean
    onOk: (order: Order) => void
    onCancel: () => void
}

const PickQuantity: React.VFC<PickQuantityProps> = ({order, product, visible, onOk, onCancel}) => {
    const [form] = Form.useForm()
    return (
        <>
            <Modal visible={visible} maskClosable={false}
                   closable={true}
                   onCancel={onCancel}
                   onOk={() => {
                       if (form.getFieldValue('qty') !== undefined) {
                           let totalPrice:number = 0
                           const myOrder: Order = {
                               ...order
                           }
                           if (myOrder && myOrder.orderLines && myOrder.orderLines.length>0 && myOrder.orderLines.map(ol => ol.menuRestaurantName).includes(product.name)) {
                               const orderLine: OrderLine = myOrder.orderLines.filter(ol => ol.menuRestaurantName === product.name)[0]
                               orderLine.quantity = form.getFieldValue('qty')
                           } else {
                               const orderLine: OrderLine = {
                                   id: undefined as unknown as number,
                                   menuRestaurantName: product.name,
                                   quantity: form.getFieldValue('qty') as number,
                                   price: product.price
                               }
                               if (!myOrder.restaurantName){
                                   myOrder.restaurantName = product.restaurantName
                                   myOrder.status = 'NEW'
                               }
                               if (!myOrder.orderLines){
                                   myOrder.orderLines = []
                               }
                               myOrder.orderLines.push(orderLine)
                           }
                           myOrder.orderLines.forEach(orderLine=>totalPrice = totalPrice +orderLine.quantity*orderLine.price)
                           myOrder.price = Math.round(totalPrice)
                           onOk(myOrder)
                           form.resetFields()
                       }
                   }}
            >
                <div>
                    <Card title="Order quantity" bordered={false} style={{width: 450}}>
                        <Form form={form} labelCol={{span: 8}}
                              wrapperCol={{span: 16}}
                              initialValues={{
                                  qty: undefined
                              }}>
                            <Form.Item label="Quantity" name="qty">
                                <InputNumber min={1} max={10}/>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>

            </Modal>
        </>
    )
}

export default PickQuantity