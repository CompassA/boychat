package org.study;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.boychat.enums.MsgType;
import org.boychat.factory.MessageFactory;
import org.junit.Assert;
import org.junit.Test;
import org.study.boychat.data.MessageRequest;
import org.study.handler.MessageHandler;
import org.study.server.BoyChatReactorServer;

import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author tomato
 * Created on 2020.11.17
 */
public class MockStickyTests {

    @Test
    public void StickyTest() throws InterruptedException, BrokenBarrierException {
        //start server
        int port = 5654;
        String ip = "127.0.0.1";
        BoyChatReactorServer boyChatReactorServer = new BoyChatReactorServer(port);
        boyChatReactorServer.start();

        //start client and send message
        final StringBuilder sentMessages = new StringBuilder();
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        new Thread(() -> new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ClientHandler())
                .connect(ip, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        Channel channel = ((ChannelFuture) future).channel();
                        for (int i = 0; i < 1000; ++i) {
                            String randomUUID = UUID.randomUUID().toString();
                            sentMessages.append(randomUUID);
                            byte[] body = MessageRequest.newBuilder()
                                    .setMessage(randomUUID)
                                    .build()
                                    .toByteArray();
                            byte[] message = MessageFactory.create(MsgType.MSG, body)
                                    .toByteArray();
                            channel.writeAndFlush(
                                    ByteBufAllocator.DEFAULT
                                            .buffer(message.length, message.length)
                                            .writeBytes(message)
                            );
                        }
                    }
                    cyclicBarrier.await();
                })
        ).start();

        //check sticky
        cyclicBarrier.await();
        boyChatReactorServer.getHandlers().stream()
                .filter(handler -> handler instanceof MessageHandler)
                .map(handler -> (MessageHandler) handler)
                .findFirst()
                .ifPresent(messageHandler -> {
                    String serverReceived = messageHandler.cleanCache();
                    Assert.assertNotEquals(serverReceived, sentMessages.toString());
                });
    }

    private static class ClientHandler extends ChannelInitializer<NioSocketChannel> {
        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
        }
    }

}
