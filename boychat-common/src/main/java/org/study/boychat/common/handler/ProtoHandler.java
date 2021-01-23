package org.study.boychat.common.handler;


import com.google.protobuf.AbstractMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.boychat.data.ChatPacket;
import org.boychat.data.core.ProtoPacketFactory;
import org.boychat.enums.MsgType;
import org.boychat.factory.CommonProtoPacketFactory;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.data.MessageResponse;

import java.util.List;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
@ChannelHandler.Sharable
public class ProtoHandler extends MessageToMessageCodec<ChatPacket, AbstractMessage> {

    public static final ProtoHandler INSTANCE = new ProtoHandler();

    private final ProtoPacketFactory protoPacketFactory = CommonProtoPacketFactory.INSTANCE;

    private ProtoHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, List<Object> out) throws Exception {
        if (msg == null) {
            ctx.channel().close();
            return;
        }
        out.add(protoPacketFactory.create(msg));
    }

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
