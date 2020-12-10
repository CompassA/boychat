package org.study.boychat.utils;

import io.netty.buffer.ByteBuf;
import org.boychat.data.ChatPacket;

/**
 * 缓冲区读写工具类
 * @author tomato
 * Created on 2020.11.21
 */
public final class ReadWriteBufferUtil {

    private ReadWriteBufferUtil() {
        throw new RuntimeException("permission denied");
    }

    /**
     * 从缓冲区度包
     * @param in 缓冲区
     * @return 报文
     */
    public static ChatPacket read(ByteBuf in) {
        int magicNumber = in.readInt();
        int version = in.readInt();
        byte serialization = in.readByte();
        long id = in.readLong();
        int type = in.readInt();
        int length = in.readInt();

        //根据头部长度读取真正的body体
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

    /**
     * 将要发送的报文写入缓冲区
     * @param out 缓冲区
     * @param packet 待写入报文
     */
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
