package org.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.boychat.constants.Constants;
import org.boychat.data.ChatPacket;
import org.boychat.data.core.ProtoPacketFactory;
import org.boychat.enums.MsgType;
import org.boychat.factory.CommonProtoPacketFactory;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.data.MessageResponse;
import org.study.boychat.logger.TomatoLogger;
import org.study.boychat.utils.ReadWriteBufferUtil;
import org.study.boychat.data.Attributes;

import java.util.UUID;

/**
 * @author fanqie
 * @date 2020/6/3
 */
@Deprecated
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(FirstClientHandler.class);

    private final ProtoPacketFactory protoPacketFactory = new CommonProtoPacketFactory();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //create request
        ChatPacket packet = protoPacketFactory.create(
                LoginRequest.newBuilder()
                        .setEmail("test")
                        .setPassword(UUID.randomUUID().toString())
                        .build()
        );
        int bufferLength = packet.getLength() + Constants.HEADER_LENGTH;
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bufferLength, bufferLength);
        ReadWriteBufferUtil.write(buffer, packet);
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChatPacket packet = ReadWriteBufferUtil.read((ByteBuf) msg);
        switch (MsgType.getById(packet.getType())) {
            case LOGIN:
                LoginResponse loginResponse = LoginResponse.parseFrom(packet.getBody());
                if (loginResponse.getSuccess()) {
                    ctx.channel().attr(Attributes.LOGIN_MARK).set(true);
                    LOGGER.info("登录成功！");
                } else {
                    LOGGER.info("登录失败！");
                }
                break;
            case MSG:
                MessageResponse messageResponse = MessageResponse.parseFrom(packet.getBody());
                LOGGER.info("收到服务端响应:" + messageResponse.getMessage());
                break;
            default:
        }
    }
}
