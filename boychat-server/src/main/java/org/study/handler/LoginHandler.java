package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.logger.TomatoLogger;
import org.study.utils.LoginUtil;

/**
 * @author tomato
 * Created on 2020.11.15
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequest> {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(LoginHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest msg) throws Exception {
        //TODO login
        LoginUtil.markLogin(ctx.channel());
        LOGGER.info("client login: account" + msg.getAccount());
        ctx.channel().writeAndFlush(
                LoginResponse.newBuilder().setSuccess(true).build()
        );
    }
}
