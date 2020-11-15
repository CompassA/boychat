package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.boychat.enums.MsgType;
import org.boychat.factory.MessageFactory;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.LoginResponse;

/**
 * @author tomato
 * Created on 2020.11.15
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest msg) throws Exception {
        //TODO login
        byte[] response = LoginResponse.newBuilder()
                .setSuccess(true)
                .build()
                .toByteArray();
        ctx.channel().writeAndFlush(MessageFactory.create(MsgType.LOGIN, response));
    }
}
