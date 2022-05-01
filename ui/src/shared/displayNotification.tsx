import {notification} from "antd";

export const displayNotification = (messageType: 'Info'|'Error'|'Success'|'Warn', message: string, durationTime: number) =>{
    const args = {
        message: messageType,
        description: message,
        duration: durationTime,
    };
    if (messageType === 'Success')
       notification.success(args)
    if (messageType === 'Info')
        notification.info(args)
    if (messageType === 'Warn')
        notification.warn(args)
    if (messageType === 'Error')
        notification.error(args)
}
