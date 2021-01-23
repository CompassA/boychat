package org.study.boychat.common.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.boychat.data.ChatPacket;
import org.boychat.enums.MsgType;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.MessageRequest;

import java.util.List;

/**
 * 将body体进行proto反序列化
 * @author tomato
 * Created on 2020.11.19
 */
public class ProtoDecoder extends MessageToMessageDecoder<ChatPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ChatPacket msg, List<Object> out) throws Exception {
        switch (MsgType.getById(msg.getType())) {
            case LOGIN:
                out.add(LoginRequest.parseFrom(msg.getBody()));
                break;
            case MSG:
                out.add(MessageRequest.parseFrom(msg.getBody()));
                break;
            default:
                ctx.channel().close();
        }
    }
}
