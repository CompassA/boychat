package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.logger.TomatoLogger;
import org.study.store.Session;
import org.study.store.SessionManager;
import org.study.store.impl.LocalSessionManager;
import org.study.store.impl.TomatoSession;
import org.study.utils.LoginUtil;

/**
 * 处理登录
 * @author tomato
 * Created on 2020.11.15
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequest> {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(LoginHandler.class);

    private static final SessionManager SESSION_MANAGER = SessionManager.getSingletonByClass(LocalSessionManager.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequest msg) throws Exception {
        Session session = TomatoSession.builder()
                .userId(msg.getAccount())
                .userName("xxx")
                .userPassword(msg.getPassword())
                .channel(ctx.channel())
                .build();
        String token = SESSION_MANAGER.saveSession(session);
        LoginUtil.markLogin(ctx.channel());
        LOGGER.info("client login: account" + msg.getAccount());
        ctx.channel().writeAndFlush(
                LoginResponse.newBuilder()
                        .setSuccess(true)
                        .setToken(token)
                        .build()
        );
    }
}
