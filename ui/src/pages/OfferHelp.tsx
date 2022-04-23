import axios from "axios";
import {UserContext, UserContextInterface} from "../App";
import React, {useState} from "react";
import {Form, Input, message, Modal} from "antd";
import {HelpPoint} from "../model/HelpPoint";

interface OfferHelpProps {
    visible:boolean,
    close: () => void
}

const OfferHelp: React.VFC<OfferHelpProps> = ({visible,close}) => {
    const value: UserContextInterface | null = React.useContext(UserContext)
    const [form] = Form.useForm()
    const submit = () => {
        if(form.getFieldValue("name")===undefined || form.getFieldValue("address")===undefined ){
            message.error("Please fill the fields")
            return
        }
        const token = value ? value.accessToken : ''
        const helpPoint:HelpPoint = {
            id: undefined as unknown as number,
            name: form.getFieldValue('name'),
            address: form.getFieldValue('address')
        }
        const url = `http://${process.env.REACT_APP_SERVER_NAME}/help/help`
        axios.post(url, helpPoint, {
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        })
        close()
        form.resetFields()
    }
    return (<>
        <Modal maskClosable={false} title={"Add help point"}
               visible={visible}
               onOk={() => {
                   submit()
               }               }
               onCancel={close}
        >
            <Form
                name="basic"
                form={form}
                labelCol={{span: 8}}
                wrapperCol={{span: 16}}
                initialValues={{
                    name: undefined,
                    address: undefined,
                }}
                autoComplete="off"
            >
                <Form.Item
                    label="Name"
                    name="name"
                    rules={[{required: true, message: 'Please input name'}]}
                >
                    <Input/>
                </Form.Item>
                <Form.Item
                    label="Address"
                    name="address"
                    rules={[{required: true, message: 'Please input address name'}]}
                >
                    <Input/>
                </Form.Item>

            </Form>

        </Modal>
    </>)
}
export default OfferHelp