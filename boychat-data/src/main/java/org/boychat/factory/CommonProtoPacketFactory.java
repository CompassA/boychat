package org.boychat.factory;

import com.google.protobuf.AbstractMessage;
import org.boychat.constants.Constants;
import org.boychat.data.ChatPacket;
import org.boychat.data.core.ProtoPacketFactory;
import org.boychat.enums.MsgType;
import org.boychat.enums.SerializationEnum;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author tomato
 * Created on 2020.11.18
 */
public final class CommonProtoPacketFactory implements ProtoPacketFactory {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    public static final CommonProtoPacketFactory INSTANCE = new CommonProtoPacketFactory();

    private CommonProtoPacketFactory() {
    }

    @Override
    public ChatPacket create(AbstractMessage message) {
        if (message == null) {
            return null;
        }
        byte[] body = message.toByteArray();
        return ChatPacket.builder()
                .magicNumber(Constants.MAGIC_NUMBER)
                .version(Constants.VERSION)
                .serialization(SerializationEnum.PROTO.getId())
                .id(ID_GENERATOR.getAndIncrement())
                .type(MsgType.getByClass(message.getClass()).getTypeId())
                .length(body.length)
                .body(body)
                .build();
    }
}
