package org.boychat.enums;

/**
 * @author fanqie
 * Created on 2020.08.15
 */
public enum MsgType {

    /** 登录命令 */
    LOGIN(1),
    MSG(2),
    ;

    private final int typeId;

    MsgType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }
}
