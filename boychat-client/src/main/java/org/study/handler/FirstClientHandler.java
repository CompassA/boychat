package org.study.handler;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.boychat.constants.Constants;
import org.boychat.enums.MsgType;
import org.study.boychat.data.LoginMsg;
import org.study.boychat.data.Message;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author fanqie
 * @date 2020/6/3
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoginMsg loginMsg = LoginMsg.newBuilder()
                .setAccount(UUID.randomUUID().toString())
                .setPassword(UUID.randomUUID().toString())
                .build();
        byte[] loginMsgBytes = loginMsg.toByteArray();
        Message message = Message.newBuilder()
                .setMagicNumber(Constants.MAGIC_NUMBER)
                .setVersion(Constants.VERSION)
                .setType(MsgType.LOGIN.getTypeId())
                .setLength(loginMsgBytes.length)
                .setBody(ByteString.copyFrom(loginMsgBytes))
                .build();
        byte[] msgBytes = message.toByteArray();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(msgBytes.length, msgBytes.length);
        buffer.writeBytes(msgBytes);
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("receive server message [%s]\n",
                ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
    }
}
