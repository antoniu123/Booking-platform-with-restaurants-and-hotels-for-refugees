import React from "react";
import {Image} from "antd";


export const Home : React.VFC = () => {
    return (
        <>
            <Image
                preview={false}
                src="bckg.jpeg"
                width= {1000}
            />
        </>
    )
}