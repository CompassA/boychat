package org.study.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.study.boychat.data.MessageResponse;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
@ChannelHandler.Sharable
public class MsgResponseHandler extends SimpleChannelInboundHandler<MessageResponse> {

    public static final MsgResponseHandler INSTANCE = new MsgResponseHandler();

    private MsgResponseHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponse messageResponse) throws Exception {
        System.out.println(formatResponse(messageResponse));
    }

    private String formatResponse(MessageResponse messageResponse) {
        return String.format("%s: %s", messageResponse.getSrcEmail(), messageResponse.getMessage());
    }
}
