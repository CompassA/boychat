package org.study.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.boychat.factory.CommonProtoPacketFactory;
import org.study.boychat.decoder.PacketDecoder;
import org.study.boychat.decoder.PacketSplitter;
import org.study.boychat.logger.TomatoLogger;
import org.study.boychat.utils.ThreadPoolUtil;
import org.study.client.data.ClientDataManager;
import org.study.client.decoder.ClientProtoDecoder;
import org.study.handler.InputHandler;
import org.study.handler.LoginHandler;
import org.study.handler.MsgResponseHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Tomato
 * Created on 2020.12.13
 */
@Getter
public class BoyChatClient {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(BoyChatClient.class);

    private final String ip;

    private final int port;

    private final ClientDataManager clientDataManager;

    private final ExecutorService singleThreadPool;

    private final Bootstrap clientBootstrap;

    private Channel channel;

    public BoyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.clientDataManager = new ClientDataManager();
        this.singleThreadPool = ThreadPoolUtil.createSinglePool();
        this.clientBootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                //decoder
                                .addLast(new PacketSplitter())
                                .addLast(new PacketDecoder())
                                .addLast(new ClientProtoDecoder())
                                //handler
                                .addLast(new LoginHandler(clientDataManager))
                                .addLast(new MsgResponseHandler());

                    }
                });
    }

    public void connect(int retryTimes) {
        clientBootstrap.connect(ip, port).addListener(future -> {
            if (future.isSuccess()) {
                LOGGER.info("connection succeeded!");
                this.channel = ((ChannelFuture) future).channel();
                startConsoleThread();
            } else if (retryTimes > 0) {
                LOGGER.info("connection failed, client will retry in 5 seconds!");
                clientBootstrap.config().group().schedule(
                        () -> connect(retryTimes - 1), 5, TimeUnit.SECONDS);
            } else {
                LOGGER.info("connection failed!");
                System.exit(0);
            }
        });
    }

    private void startConsoleThread() {
        singleThreadPool.execute(
                new InputHandler(channel, new CommonProtoPacketFactory(), clientDataManager));
    }

    public void stopConsoleThread() {
        singleThreadPool.shutdown();
    }
}
