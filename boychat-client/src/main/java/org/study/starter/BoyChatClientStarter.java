package org.study.starter;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.ByteString;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.boychat.constants.Constants;
import org.boychat.enums.MsgType;
import org.boychat.factory.MessageFactory;
import org.study.boychat.data.Message;
import org.study.boychat.data.MessageRequest;
import org.study.constans.Attributes;
import org.study.handler.FirstClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/6/1
 */
public class BoyChatClientStarter {

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });
        connect(bootstrap, "127.0.0.1", 9090, 3);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retryTimes) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("connection succeeded!");
                startConsoleThread(((ChannelFuture) future).channel());
            } else if (retryTimes > 0) {
                System.out.println("connection failed, client will retry in 5 seconds!");
                bootstrap.config().group().schedule(
                        () -> connect(bootstrap, host, port, retryTimes - 1), 5, TimeUnit.SECONDS);
            } else {
                System.out.println("connection failed!");
                System.exit(0);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("console-thread-pool-%d")
                .build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(() -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (!Thread.interrupted()) {
                if (channel.attr(Attributes.LOGIN_MARK).get() != null) {
                    try {
                        byte[] body = MessageRequest.newBuilder()
                                .setMessage(consoleReader.readLine())
                                .build()
                                .toByteArray();
                        byte[] message = MessageFactory.create(MsgType.MSG, body).toByteArray();

                        channel.writeAndFlush(
                                ByteBufAllocator.DEFAULT.buffer(message.length, message.length)
                                        .writeBytes(message)
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        singleThreadPool.shutdown();
    }
}
