package org.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.boychat.enums.MsgType;
import org.boychat.factory.MessageFactory;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.data.Message;
import org.study.boychat.data.MessageResponse;
import org.study.constans.Attributes;

import java.util.UUID;

/**
 * @author fanqie
 * @date 2020/6/3
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //create request
        LoginRequest loginMsg = LoginRequest.newBuilder()
                .setAccount(UUID.randomUUID().toString())
                .setPassword(UUID.randomUUID().toString())
                .build();
        byte[] loginMsgBytes = loginMsg.toByteArray();

        //create byte buf
        byte[] msgBytes = MessageFactory.create(MsgType.LOGIN, loginMsgBytes).toByteArray();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(msgBytes.length, msgBytes.length);
        buffer.writeBytes(msgBytes);

        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //create byte buf
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] msgBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBytes);

        //deserialize
        Message message = Message.parseFrom(msgBytes);
        switch (message.getType()) {
            case 1:
                LoginResponse loginResponse = LoginResponse.parseFrom(message.getBody());
                if (loginResponse.getSuccess()) {
                    ctx.channel().attr(Attributes.LOGIN_MARK).set(true);
                    System.out.println("登录成功！");
                } else {
                    System.out.println("登录失败！");
                }
                break;
            case 2:
                MessageResponse messageResponse = MessageResponse.parseFrom(message.getBody());
                System.out.println("收到服务端响应:" + messageResponse.getMessage());
                break;
            default:
        }
    }
}
