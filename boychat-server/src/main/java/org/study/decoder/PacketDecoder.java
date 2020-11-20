package org.study.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.boychat.data.ChatPacket;

import java.util.List;

/**
 * @author tomato
 * Created on 2020.11.19
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNumber = in.readInt();
        int version = in.readInt();
        byte serialization = in.readByte();
        long id = in.readLong();
        int type = in.readInt();
        int length = in.readInt();
        byte[] body = new byte[length];
        in.readBytes(body);
        ChatPacket packet = ChatPacket.builder()
                .magicNumber(magicNumber)
                .version(version)
                .length(length)
                .serialization(serialization)
                .id(id)
                .type(type)
                .body(body)
                .build();
        out.add(packet);
    }
}
