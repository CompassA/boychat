package org.study.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.study.boychat.common.data.MessageRequest;
import org.study.boychat.common.data.MessageResponse;
import org.study.boychat.common.logger.TomatoLogger;
import org.study.store.SessionManager;
import org.study.store.impl.LocalSessionManager;

/**
 * 消息转发
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(MessageHandler.class);

    private static final SessionManager SESSION_MANAGER = SessionManager.getSingletonByClass(LocalSessionManager.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request) throws Exception {
        for (String userId : SESSION_MANAGER.getAllUserId()) {
            SESSION_MANAGER.getSessionByUserId(userId).ifPresent(session -> {
                MessageResponse response = MessageResponse.newBuilder()
                        .setMessage(request.getMessage())
                        //消息来源
                        .setSrcEmail(request.getSrcEmail())
                        //消息要发送给谁
                        .setDesEmail(session.getEmail())
                        .build();
                session.getChannel().writeAndFlush(response);
            });
        }
    }
}
