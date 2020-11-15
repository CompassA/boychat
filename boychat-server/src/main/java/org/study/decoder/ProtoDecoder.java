package org.study.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.Message;
import org.study.boychat.data.MessageRequest;

import java.util.List;

/**
 * @author tomato
 * Created on 2020.11.15
 */
public class ProtoDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] msg = new byte[in.readableBytes()];
        in.readBytes(msg);
        Message message = Message.parseFrom(msg);
        switch (message.getType()) {
        case 1:
            out.add(LoginRequest.parseFrom(message.getBody()));
            break;
        case 2:
            out.add(MessageRequest.parseFrom(message.getBody()));
            break;
        default:
        }

    }
}
