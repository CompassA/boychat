package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.boychat.data.Attributes;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.logger.TomatoLogger;
import org.study.client.data.ClientDataManager;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginResponse> {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(LoginHandler.class);

    private final ClientDataManager clientDataManager;

    public LoginHandler(ClientDataManager clientDataManager) {
        this.clientDataManager = clientDataManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponse loginResponse) throws Exception {
        if (loginResponse.getSuccess()) {
            ctx.channel().attr(Attributes.LOGIN_MARK).set(true);
            clientDataManager.processAfterResponse(loginResponse.getToken());
            LOGGER.info("登录成功！");
        } else {
            LOGGER.info("登录失败！");
        }
    }
}
