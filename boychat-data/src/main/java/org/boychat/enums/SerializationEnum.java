package org.boychat.enums;

import lombok.Getter;

/**
 * 序列化方式
 * @author tomato
 * Created on 2020.11.18
 */
@Getter
public enum SerializationEnum {

    /**
     * proto buffer
     */
    PROTO((byte) 1),
    /**
     * json
     */
    JSON((byte) 2),
    ;

    SerializationEnum(byte id) {
        this.id = id;
    }

    private final byte id;
}
