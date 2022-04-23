import {UserContext, UserContextInterface} from "../App";
import React, {useRef} from "react";
import {message, Modal, Row} from "antd";
import emailjs from '@emailjs/browser'

const h1Styles: React.CSSProperties = {
    alignItems: 'baseline',
    position: 'relative',
    right: 0,
    bottom: '2rem',
    padding: '0.5rem',
    fontFamily: 'sans-serif',
    fontSize: '1rem'
};

interface CustomerSupportProps {
    visible:boolean,
    close: () => void
}

const CustomerSupport: React.VFC<CustomerSupportProps> = ({visible,close}) => {
    const value: UserContextInterface | null = React.useContext(UserContext)
    const form = useRef(null)


    const sendEmail = (e:any) => {
        e.preventDefault();
        if (e.message === undefined){
            message.error("Please fill the fields")
            return
        }
        emailjs.sendForm(`service_c0cggap`,
            process.env.REACT_APP_TEMPLATE_ID?process.env.REACT_APP_TEMPLATE_ID : '' ,
            form.current ? form.current :'', '--hYWIXb57RG9jcUa')
            .then((result) => {
                    message.info(result.text)
                },
                (error:Error) => {
                    message.error(error.message)
                });
    };

    return (<>
      <div >
        <Modal maskClosable={false} title={"Add help point"}
               visible={visible}
               footer={null}
               onCancel={close}
        >
            <form style={h1Styles} ref={form} onSubmit={sendEmail}>
                <Row>
                    <label>From Name</label>
                    <input type="text" name="from_name" />
                </Row>
                <Row>
                    <label>To Email</label>
                    <input type="email" name="to_name" />
                </Row>
                <Row>
                    <label>Message</label>
                    <input type="text" name="message" />
                </Row>
                <Row>
                    <button type={"submit"} value="Send" > Send
                    </button>
                </Row>
            </form>

        </Modal>
      </div>
    </>)
}
export default CustomerSupport