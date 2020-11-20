package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.study.boychat.data.MessageRequest;
import org.study.boychat.data.MessageResponse;

/**
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request)
            throws Exception {
        String clientMessage = request.getMessage();
        System.out.printf("接收到客户端消息: %s\n", clientMessage);

        ctx.channel().writeAndFlush(
                MessageResponse.newBuilder().setMessage(clientMessage).build()
        );
    }
}
