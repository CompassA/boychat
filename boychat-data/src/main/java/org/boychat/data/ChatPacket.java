package org.boychat.data;

import lombok.Builder;
import lombok.Getter;

/**
 * @author tomato
 * Created on 2020.11.18
 */
@Getter
@Builder
public class ChatPacket {

    /**
     * 魔法值
     */
    private final int magicNumber;

    /**
     * 版本
     */
    private final int version;

    /**
     * 序列化类型
     */
    private final byte serialization;

    /**
     * 消息id
     */
    private final long id;

    /**
     * 消息类型
     */
    private final int type;

    /**
     * body长度
     */
    private final int length;

    /**
     * 消息体
     */
    private final byte[] body;
}
