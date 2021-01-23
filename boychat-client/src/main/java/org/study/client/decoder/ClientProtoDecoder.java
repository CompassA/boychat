package org.study.client.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.boychat.data.ChatPacket;
import org.boychat.enums.MsgType;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.data.MessageResponse;

import java.util.List;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
public class ClientProtoDecoder extends MessageToMessageDecoder<ChatPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ChatPacket msg, List<Object> out) throws Exception {
        switch (MsgType.getById(msg.getType())) {
            case LOGIN:
                out.add(LoginResponse.parseFrom(msg.getBody()));
                break;
            case MSG:
                out.add(MessageResponse.parseFrom(msg.getBody()));
                break;
            default:
                ctx.channel().close();
        }
    }
}
