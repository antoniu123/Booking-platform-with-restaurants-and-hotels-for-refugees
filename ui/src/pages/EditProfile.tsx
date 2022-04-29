import axios from "axios";
import {UserContext, UserContextInterface} from "../App";
import React, {useState} from "react";
import {
    Alert, Avatar,
    Button,
    Card,
    Col,
    Form,
    Image,
    Input,
    InputNumber,
    message,
    Modal,
    notification,
    Result,
    Row,
    Spin
} from "antd";
import {Profile} from "../model/Profile";
import {assign, Machine} from "xstate";
import {useMachine} from "@xstate/react";
import {UserOutlined} from "@ant-design/icons";

const myAvailablePictures: string[] = ['1.png', '2.jpg', '3.png', '4.jpg']


const EditProfile: React.VFC = () => {
    const value: UserContextInterface | null = React.useContext(UserContext)
    const [profileState, send] = useMachine(createProfileMachine(value))
    const [form] = Form.useForm()
    const [imageSelector, setImageSelector] = useState(false)

    const onFinish = () => {
        if (form.getFieldValue("nickname") === undefined) {
            message.error("Please fill the fields")
            return
        }
        const profile: Profile = {
            id: form.getFieldValue('id'),
            nickname: form.getFieldValue('nickname'),
            image: form.getFieldValue('image')
        }
        send({type: 'SAVE', payload: {profile: profile}})
        form.resetFields()
        const args = {
            message: 'Info',
            description: "saving is done",
            duration: 1,
        };
        notification.success(args);
    }

    const onFinishFailed = (errorInfo: any) => {
        const args = {
            message: 'Error',
            description: errorInfo,
            duration: 1,
        };
        notification.error(args);
    }

    return (
        <>
            {(profileState.matches('loading') ||
                profileState.matches('saving')) && (
                <>
                    <Spin>
                        <Alert message="Please wait for loading" type="info"/>
                    </Spin>
                </>
            )}

            {profileState.matches('resolved') && (
                <>
                    <Card
                        style={{width: 350}}
                        cover={<>  {profileState.context.profile?.image ?
                                   <img alt="avatar" src={profileState.context.profile?.image}
                                    onClick={() => setImageSelector(true)}/> :
                                    <>
                                        <Avatar size={"large"} style={{backgroundColor: '#87d068'}}
                                            icon={<UserOutlined onClick={() => setImageSelector(true)}/>}/>
                                    </>
                                    }
                               </>}
                    >
                        <Form
                            name="basic"
                            form={form}
                            labelCol={{span: 8}}
                            wrapperCol={{span: 16}}
                            onFinish={onFinish}
                            onFinishFailed={onFinishFailed}
                            initialValues={{
                                id: profileState.context.profile ? profileState.context.profile.id : undefined,
                                nickname: profileState.context.profile ? profileState.context.profile.nickname : undefined,
                                image: profileState.context.profile ? profileState.context.profile.image : undefined
                            }}
                            autoComplete="off"
                        >
                            <Row align="middle">
                                <Col span={20}>
                                    <Form.Item
                                        name="id"
                                        hidden
                                    >
                                        <InputNumber/>
                                    </Form.Item>
                                    <Form.Item
                                        label="Nickname"
                                        name="nickname"
                                        rules={[{required: true, message: 'Please input desired nickname'}]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <Form.Item
                                        name="image"
                                        hidden={true}
                                    >
                                        <Input/>
                                    </Form.Item>
                                </Col>

                            </Row>
                            <Row>
                                <Form.Item>
                                    <Button type="primary" htmlType="submit">
                                        Submit
                                    </Button>
                                </Form.Item>
                            </Row>
                        </Form>
                    </Card>

                    {imageSelector &&
                        <Modal visible={imageSelector} maskClosable={false} closable={true}
                               onCancel={() => setImageSelector(false)}
                               onOk={() => {
                                   if (profileState.context && profileState.context.profile) {
                                       profileState.context.profile.image = form.getFieldValue('image')
                                   }
                                   setImageSelector(false)
                               }
                               }>
                            <Row gutter={[16, 16]}>
                                {myAvailablePictures.map((current: string, index: number, pictures: string[]) => (
                                    <Col key={index} xs={{span: 6, offset: 1}} lg={{span: 4, offset: 1}}>
                                        <Image width={50} preview={false} key={index} src={current} onClick={() => {
                                            form.setFieldsValue({image: current})
                                        }}/>
                                    </Col>
                                ))}
                            </Row>
                        </Modal>
                    }
                </>)}

            {profileState.matches('rejected') && (
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
        </>)
}
export default EditProfile

interface ProfileMachineContext {
    profile: Profile
}

interface ProfileMachineSchema {
    context: ProfileMachineContext
    states: {
        loading: {}
        resolved: {}
        rejected: {}
        saving: {}
    }
}

type ProfileMachineEvent = | { type: 'RETRY' }
    | { type: 'SAVE'; payload: { profile: Profile } }

const createProfileMachine = (userContext: UserContextInterface | null) => Machine<ProfileMachineContext, ProfileMachineSchema, ProfileMachineEvent>(
    {
        id: 'hotel-machine',
        context: {
            profile: {} as Profile
        },
        initial: 'loading',
        on: {
            RETRY: 'loading'
        },
        states: {
            loading: {
                invoke: {
                    src: 'loadData',
                    onDone: {
                        target: 'resolved',
                        actions: assign((context, event) => {
                            return {
                                profile: event.data.data
                            }
                        })
                    },
                    onError: {
                        target: 'rejected'
                    }
                }
            },
            resolved: {
                on: {
                    RETRY: {
                        target: 'loading'
                    },
                    SAVE: {
                        target: 'saving'
                    }
                }
            },
            rejected: {
                on: {
                    RETRY: {
                        target: 'loading'
                    }
                }
            },
            saving: {
                invoke: {
                    src: 'save',
                    onDone: {
                        target: 'loading'
                    },
                    onError: {
                        target: 'resolved'
                    }
                }
            }
        }
    },
    {
        services: {
            loadData: () => {
                const url = `http://${process.env.REACT_APP_SERVER_NAME}/profile/my`
                const token = userContext ? userContext.accessToken : ''
                return axios.get(url, {
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                })
                    .then(response => response)
                    .catch(err => {
                        console.error(err)
                        return err
                    })
            },
            save: (id, event) => {
                if (event.type === 'SAVE') {
                    const token = userContext ? userContext.accessToken : ''
                    const url = `http://${process.env.REACT_APP_SERVER_NAME}/profile/`
                    return axios.post(url, event.payload.profile, {
                        headers: {
                            "Authorization": `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    })
                }
                return Promise.reject("someting wrong")
            }
        }
    }
)