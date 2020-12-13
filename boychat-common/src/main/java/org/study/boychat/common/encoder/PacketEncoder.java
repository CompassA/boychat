package org.study.boychat.common.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.boychat.data.ChatPacket;
import org.study.boychat.common.utils.ReadWriteBufferUtil;

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
        ReadWriteBufferUtil.write(out, msg);
    }
}
