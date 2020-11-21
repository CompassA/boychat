package org.study.boychat.utils;

import io.netty.buffer.ByteBuf;
import org.boychat.data.ChatPacket;

/**
 * @author tomato
 * Created on 2020.11.21
 */
public final class ReadWriteBufferUtil {

    private ReadWriteBufferUtil() {
        throw new RuntimeException("permission denied");
    }

    public static ChatPacket read(ByteBuf in) {
        int magicNumber = in.readInt();
        int version = in.readInt();
        byte serialization = in.readByte();
        long id = in.readLong();
        int type = in.readInt();
        int length = in.readInt();
        byte[] body = new byte[length];
        in.readBytes(body);
        return ChatPacket.builder()
                .magicNumber(magicNumber)
                .version(version)
                .length(length)
                .serialization(serialization)
                .id(id)
                .type(type)
                .body(body)
                .build();
    }

    public static void write(ByteBuf out, ChatPacket packet) {
        out.writeInt(packet.getMagicNumber());
        out.writeInt(packet.getVersion());
        out.writeByte(packet.getSerialization());
        out.writeLong(packet.getId());
        out.writeInt(packet.getType());
        out.writeInt(packet.getBody().length);
        out.writeBytes(packet.getBody());
    }
}
