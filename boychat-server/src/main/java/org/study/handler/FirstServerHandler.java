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
import org.study.boychat.data.MessageRequest;
import org.study.boychat.data.MessageResponse;

/**
 * @author fanqie
 * @date 2020/6/3
 */
@Deprecated
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //get bytes
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] msgBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBytes);

        //deserialize
        Message message = Message.parseFrom(msgBytes);
        switch (message.getType()) {
            case 1:
                handlerLogin(ctx, LoginRequest.parseFrom(message.getBody()));
                break;
            case 2:
                handlerMessage(ctx, MessageRequest.parseFrom(message.getBody()));
                break;
            default:
        }
    }

    private void handlerLogin(ChannelHandlerContext ctx, LoginRequest request) {
        //TODO login
        byte[] response = LoginResponse.newBuilder()
                .setSuccess(true)
                .build()
                .toByteArray();
        byte[] message = MessageFactory.create(MsgType.LOGIN, response).toByteArray();

        ctx.channel().writeAndFlush(
                ByteBufAllocator.DEFAULT
                        .buffer(message.length, message.length)
                        .writeBytes(message)
        );
    }

    private void handlerMessage(ChannelHandlerContext ctx, MessageRequest request) {
        String clientMessage = request.getMessage();
        System.out.printf("接收到客户端消息: %s\n", clientMessage);

        byte[] response = MessageResponse.newBuilder()
                .setMessage(clientMessage)
                .build()
                .toByteArray();
        byte[] message = MessageFactory.create(MsgType.MSG, response).toByteArray();

        ctx.channel().writeAndFlush(
                ByteBufAllocator.DEFAULT
                        .buffer(message.length, message.length)
                        .writeBytes(message)
        );
    }
}
