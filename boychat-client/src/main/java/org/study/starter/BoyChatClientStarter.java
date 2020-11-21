package org.study.starter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;
import org.boychat.constants.Constants;
import org.boychat.data.ChatPacket;
import org.boychat.data.core.ProtoPacketFactory;
import org.boychat.factory.CommonProtoPacketFactory;
import org.study.boychat.data.MessageRequest;
import org.study.boychat.logger.TomatoLogger;
import org.study.boychat.utils.ReadWriteBufferUtil;
import org.study.boychat.utils.ThreadPoolUtil;
import org.study.constans.Attributes;
import org.study.handler.FirstClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * @date 2020/6/1
 */
public class BoyChatClientStarter {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(BoyChatClientStarter.class);

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
                LOGGER.info("connection succeeded!");
                startConsoleThread(((ChannelFuture) future).channel());
            } else if (retryTimes > 0) {
                LOGGER.info("connection failed, client will retry in 5 seconds!");
                bootstrap.config().group().schedule(
                        () -> connect(bootstrap, host, port, retryTimes - 1), 5, TimeUnit.SECONDS);
            } else {
                LOGGER.info("connection failed!");
                System.exit(0);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        ExecutorService singleThreadPool = ThreadPoolUtil.createSinglePool();
        ProtoPacketFactory packetFactory = new CommonProtoPacketFactory();
        singleThreadPool.execute(() -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (!Thread.interrupted()) {
                if (channel.attr(Attributes.LOGIN_MARK).get() != null) {
                    String oneLine = StringUtil.EMPTY_STRING;
                    try {
                        oneLine = consoleReader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ChatPacket packet = packetFactory.create(
                            MessageRequest.newBuilder().setMessage(oneLine).build());
                    int bufferLen = packet.getLength() + Constants.HEADER_LENGTH;
                    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(bufferLen, bufferLen);
                    ReadWriteBufferUtil.write(byteBuf, packet);
                    channel.writeAndFlush(byteBuf);
                }
            }
        });
        singleThreadPool.shutdown();
    }
}
