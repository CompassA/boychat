package org.study.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.study.decoder.ProtoDecoder;
import org.study.encoder.ProtoEncoder;
import org.study.handler.LoginHandler;
import org.study.handler.MessageHandler;

/**
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
public class BoyChatReactorServer {

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
                .childHandler(new BoyChatChannelInitializer());
    }

    public void start() {
        serverBootstrap.bind(bindPort);
    }

    private static class BoyChatChannelInitializer extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ProtoDecoder())
                    .addLast(new LoginHandler())
                    .addLast(new MessageHandler())
                    .addLast(new ProtoEncoder());
        }
    }
}
