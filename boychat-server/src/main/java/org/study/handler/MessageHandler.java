package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.study.boychat.data.MessageRequest;
import org.study.boychat.data.MessageResponse;
import org.study.boychat.logger.TomatoLogger;

/**
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request)
            throws Exception {
        String clientMessage = request.getMessage();
        LOGGER.info("received message from client: " + clientMessage);
        ctx.channel().writeAndFlush(
                MessageResponse.newBuilder().setMessage(clientMessage).build()
        );
    }
}
