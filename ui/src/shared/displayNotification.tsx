import {notification} from "antd";

export const displayNotification = (messageType: 'Info'|'Error'|'Success'|'Warn', message: string, durationTime: number) =>{
    const args = {
        message: messageType,
        description: message,
        duration: durationTime,
    };
    notification.success(args);
}
