package org.study.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.boychat.enums.MsgType;
import org.boychat.factory.MessageFactory;
import org.study.boychat.data.MessageRequest;
import org.study.boychat.data.MessageResponse;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequest> {

    private final static Queue<String> MESSAGE_CACHE = new LinkedList<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequest request)
            throws Exception {
        String clientMessage = request.getMessage();
        System.out.printf("接收到客户端消息: %s\n", clientMessage);
        addCache(clientMessage);

        byte[] response = MessageResponse.newBuilder()
                .setMessage(clientMessage)
                .build()
                .toByteArray();

        ctx.channel().writeAndFlush(MessageFactory.create(MsgType.MSG, response));
    }

    private void addCache(String message) {
        MESSAGE_CACHE.offer(message);
    }

    public String cleanCache() {
        StringBuilder builder = new StringBuilder();
        while (!MESSAGE_CACHE.isEmpty()) {
            builder.append(MESSAGE_CACHE.poll());
        }
        return builder.toString();
    }
}
