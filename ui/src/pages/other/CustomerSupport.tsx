import React, {useRef} from "react";
import {Card, message, Modal} from "antd";
import emailjs from '@emailjs/browser'

const h1Styles: React.CSSProperties = {
    position: 'relative',
    right: 0,
    bottom: '2.5rem',
    padding: '2rem',
    fontFamily: 'sans-serif',
    fontSize: '1rem'
};

const h1StylesLabel: React.CSSProperties = {
    display: 'inline-block',
    float: 'left',
    clear: 'left',
    width: '100px',
    textAlign: 'right',
    padding: '0.25rem',
    fontFamily: 'sans-serif',
    fontSize: '1rem'
};

const h1StylesInput: React.CSSProperties = {
    display: 'inline-block',
    float: 'left',
    padding: '0.15rem',
    fontFamily: 'sans-serif',
    fontSize: '1rem'
};

const h1StylesButton: React.CSSProperties = {
    alignContent:'center',
    height:'48',
    textAlign:'left',
    fontFamily: 'sans-serif',
    fontSize: '1rem',
    left: 125,
    paddingLeft: '5px',
    paddingRight: '5px',
    color: 'white',
    background: '#30a2c2',
    border: '2px outset #d7b9c9'
};

interface CustomerSupportProps {
    visible:boolean,
    close: () => void
}

const CustomerSupport: React.VFC<CustomerSupportProps> = ({visible,close}) => {
    const form = useRef<HTMLFormElement>(null)


    const sendEmail = (e:any) => {
        e.preventDefault();
        if (!form.current){
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
        form.current.reset()
    };

    return (<>
        <div >
            <Modal maskClosable={false} title={"Send message"} width={400}
                   visible={visible}
                   footer={null}
                   onCancel={close}
            >
                <form style={h1Styles} ref={form} onSubmit={sendEmail} >

                    <Card title="Customer support" extra={<button style={h1StylesButton} type={"submit"} value="Send" > Send
                    </button>} style={{ width: 300 }}>
                        <label style={h1StylesLabel}>From Name:</label><input key="from_name" style={h1StylesInput} type="text" name="from_name" />

                        <label hidden={true} style={h1StylesLabel}>To Email:</label><input hidden={true} defaultValue={"bogdan.antoniu1999@gmail.com"} key="to_name" style={h1StylesInput} type="email" name="to_name" />

                        <label style={h1StylesLabel}>Message:</label><textarea key="message" style={h1StylesInput} rows={4} name="message" />
                    </Card>
                </form>

            </Modal>
        </div>
    </>)
}
export default CustomerSupport