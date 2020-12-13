package org.boychat.enums;

import com.google.protobuf.AbstractMessage;
import lombok.Getter;
import org.study.boychat.common.data.LoginRequest;
import org.study.boychat.common.data.LoginResponse;
import org.study.boychat.common.data.MessageRequest;
import org.study.boychat.common.data.MessageResponse;

/**
 * @author fanqie
 * Created on 2020.08.15
 */
@Getter
public enum MsgType {

    /**
     * 未知
     */
    UNKNOWN(0),
    /**
     * 登录命令
     */
    LOGIN(1),
    /**
     * 聊天消息
     */
    MSG(2),
    ;

    private final int typeId;

    MsgType(int typeId) {
        this.typeId = typeId;
    }

    public static MsgType getById(int typeId) {
        for (MsgType value : values()) {
            if (value.typeId == typeId) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public static MsgType getByClass(Class<? extends AbstractMessage> clazz) {
        if (clazz == LoginRequest.class || clazz == LoginResponse.class) {
            return LOGIN;
        }
        if (clazz == MessageRequest.class || clazz == MessageResponse.class) {
            return MSG;
        }
        return UNKNOWN;
    }
}
