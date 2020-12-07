package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.study.boychat.logger.TomatoLogger;
import org.study.utils.LoginUtil;

/**
 * @author Tomato
 * Created on 2020.12.07
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final TomatoLogger LOGGER = TomatoLogger.getLogger(AuthHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.isLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (LoginUtil.isLogin(ctx.channel())) {
            LOGGER.info("user has login");
        }
    }
}
