import React from "react";
import {HelpPoint} from "../../model/HelpPoint";
import {assign, Machine} from "xstate";
import axios from "axios";
import {useMachine} from "@xstate/react";
import {Alert, Button, Result, Spin} from "antd";
import {LoadingOutlined} from "@ant-design/icons";
import {UserContext, UserContextInterface} from '../../App'
import Geocode from "./Geocode";
import './map.css'


export const Help: React.VFC = () => {

    const antIcon = <LoadingOutlined style={{fontSize: 24}} spin/>

    const value:UserContextInterface|null = React.useContext(UserContext)
    const [helpState, send] = useMachine(createHelpMachine(value))

    return (
        <>
            {helpState.matches('loading') && (
                <Spin indicator={antIcon} tip="Loading...">
                    <Alert message="Please wait" type="info"/>
                </Spin>
            )}
            {helpState.matches('idle') && (
                // <MapView basemap={"streets"} x={26.09} y={44.43} height={800} zoom={5.5}/>
                <Geocode
                    centerX={26.09} centerY={44.43} zoom={9}
                    addresses={helpState.context.helpPoints && helpState.context.helpPoints.length>0 ?
                        helpState.context.helpPoints.map((hp:HelpPoint)=>hp.address) : []}
                />
            )}

            {helpState.matches('rejected') && (
                <Result
                    status="error"
                    title="Loading data failed"
                    subTitle="Please try again. If Problem persis contact administrator"
                    extra={
                        <Button
                            onClick={() => {
                                send({
                                    type: 'RETRY'
                                })
                            }}
                        >
                            Retry
                        </Button>
                    }
                />
            )}

        </>)
}

interface HelpMachineContext {
    helpPoints: Array<HelpPoint>
}

interface HelpMachineSchema {
    context: HelpMachineContext
    states: {
        loading: {}
        idle: {}
        rejected: {}
    }
}

type HelpMachineEvent =
    | { type: 'RETRY' }

const createHelpMachine = (userContext:UserContextInterface|null) =>
    Machine<HelpMachineContext, HelpMachineSchema, HelpMachineEvent>(
        {
            id: 'user-machine',
            context: {
                helpPoints: []
            },
            initial: 'loading',
            states: {
                loading: {
                    invoke: {
                        id: 'loading',
                        src: 'loadData',
                        onDone: {
                            target: 'idle',
                            actions: assign((context, event) => {
                                return {
                                    helpPoints: event.data.data
                                }
                            })
                        },
                        onError: {
                            target: 'rejected'
                        }
                    }
                },
                idle: {
                    on: {
                        RETRY: {
                            target: 'loading'
                        }
                    }
                },
                rejected: {
                    on: {
                        RETRY: {
                            target: 'loading'
                        }
                    }
                }
            }
        },
        {
            services: {
                loadData: (_, event) => {
                    const token = userContext ? userContext.accessToken : ''
                    const url = `http://${process.env.REACT_APP_SERVER_NAME}/help/all`
                    return async () => axios
                        .get(url, {headers: {"Authorization": `Bearer ${token}`, "Content-Type": "application/json"}})
                        .then((ret) => Promise.resolve(ret)
                        )
                        .catch((err) => {
                            return Promise.reject(err)
                        })
                }
            }
        }
    )