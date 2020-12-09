package org.study.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.boychat.factory.CommonProtoPacketFactory;
import org.study.boychat.decoder.PacketDecoder;
import org.study.boychat.decoder.PacketSplitter;
import org.study.boychat.decoder.ProtoDecoder;
import org.study.boychat.encoder.PacketEncoder;
import org.study.boychat.encoder.ProtoEncoder;
import org.study.boychat.logger.TomatoLogger;
import org.study.handler.AuthHandler;
import org.study.handler.LoginHandler;
import org.study.handler.MessageHandler;

/**
 * @author tomato
 * Created on 2020.11.18
 */
public class BoyChatReactorServer {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(BoyChatReactorServer.class);

    private final int bindPort;

    private final NioEventLoopGroup bossGroup;

    private final NioEventLoopGroup workerGroup;

    private final ServerBootstrap serverBootstrap;

    public BoyChatReactorServer(int bindPort) {
        this.bindPort = bindPort;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                //decoder
                                .addLast(new PacketSplitter())
                                .addLast(new PacketDecoder())
                                .addLast(new ProtoDecoder())
                                //handler
                                .addLast(new LoginHandler())
                                .addLast(new AuthHandler())
                                .addLast(new MessageHandler())
                                //encoder
                                .addLast(new PacketEncoder())
                                .addLast(new ProtoEncoder(new CommonProtoPacketFactory()));
                    }
                });
    }

    public void start() {
        serverBootstrap.bind(bindPort);
        LOGGER.info("server started on port " + bindPort);
    }
}
