package org.study.client.data;

/**
 * 客户端状态
 * @author Tomato
 * Created on 2020.12.13
 */
public enum ClientState {
    /**
     * 待登录
     */
    TO_LOGIN,
    /**
     * 登录包已经发送，等待服务端响应
     */
    WAIT_RESPONSE,
    /**
     * 与服务端建立连接
     */
    CONNECTED,
    ;
}
