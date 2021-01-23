package org.study.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.study.boychat.data.Message;

/**
 * @author tomato
 * Created on 2020.11.15
 */
public class TestProtoEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg != null) {
            out.writeBytes(msg.toByteArray());
        }
    }
}
