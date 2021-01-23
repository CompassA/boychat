package org.boychat.factory;

import com.google.protobuf.ByteString;
import org.boychat.constants.Constants;
import org.boychat.enums.MsgType;
import org.study.boychat.data.Message;

/**
 * @author fanqie
 * Created on 2020.08.16
 */
public final class MessageFactory {

    private MessageFactory() {
    }

    public static Message create(final MsgType type, final byte[] body) {
        return Message.newBuilder()
                .setMagicNumber(Constants.MAGIC_NUMBER)
                .setVersion(Constants.VERSION)
                .setType(type.getTypeId())
                .setLength(body.length)
                .setBody(ByteString.copyFrom(body))
                .build();
    }
}
