package org.study.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.boychat.data.ChatPacket;

/**
 * @author tomato
 * Created on 2020.11.18
 */
public class PacketEncoder extends MessageToByteEncoder<ChatPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ChatPacket msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getBody() == null) {
            ctx.channel().close();
            return;
        }
        out.writeInt(msg.getMagicNumber());
        out.writeInt(msg.getVersion());
        out.writeByte(msg.getSerialization());
        out.writeLong(msg.getId());
        out.writeInt(msg.getType());
        out.writeInt(msg.getBody().length);
        out.writeBytes(msg.getBody());
    }
}
