package org.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.study.DateUtil;
import org.study.boychat.data.LoginMsg;
import org.study.boychat.data.Message;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author fanqie
 * @date 2020/6/3
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //get bytes
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] msgBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBytes);

        //deserialize
        Message message = Message.parseFrom(msgBytes);
        System.out.println(message.toString());
        LoginMsg loginMsg = LoginMsg.parseFrom(message.getBody());
        System.out.println(loginMsg);

        //response
        ctx.channel().writeAndFlush("msg received!");
    }


}
