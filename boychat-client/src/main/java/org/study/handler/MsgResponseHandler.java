package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.boychat.data.MessageResponse;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
public class MsgResponseHandler extends SimpleChannelInboundHandler<MessageResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponse messageResponse) throws Exception {
        System.out.println(formatResponse(messageResponse));
    }

    private String formatResponse(MessageResponse messageResponse) {
        return String.format("%s: %s", messageResponse.getSrcEmail(), messageResponse.getMessage());
    }
}
