package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.study.boychat.data.MessageRequest;
import org.study.boychat.data.MessageResponse;
import org.study.boychat.logger.TomatoLogger;
import org.study.store.SessionManager;
import org.study.store.impl.LocalSessionManager;

/**
 * 消息转发
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(MessageHandler.class);

    private static final SessionManager sessionManager = SessionManager.getSingletonByClass(LocalSessionManager.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request) throws Exception {
        String clientMessage = request.getMessage();
        String desUserId = request.getDesUserId();
        sessionManager.getSessionByUserId(desUserId).ifPresent(session ->
                session.getChannel().writeAndFlush(
                        MessageResponse.newBuilder()
                                .setMessage(clientMessage)
                                .setSrcUserId(session.getUserId())
                                .setDesUserId(request.getSrcUserId())
                                .build()
                )
        );
    }
}
