package org.study.common;

import com.google.common.collect.Lists;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.study.handler.LoginHandler;

import java.util.List;

/**
 * @author tomato
 * Created on 2020.11.15
 */
@Getter
public class TestBoyChatReactorServer {

    private final int bindPort;

    private final NioEventLoopGroup bossGroup;

    private final NioEventLoopGroup workerGroup;

    private final ServerBootstrap serverBootstrap;

    private final List<ChannelHandler> handlers;

    public TestBoyChatReactorServer(int bindPort) {
        this.bindPort = bindPort;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.handlers = Lists.newArrayList(
                new TestProtoDecoder(),
                new LoginHandler(),
                new TestMessageHandler(),
                new TestProtoEncoder());
        this.serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new BoyChatChannelInitializer(handlers));
    }

    public void start() {
        serverBootstrap.bind(bindPort);
    }

    @AllArgsConstructor
    private static class BoyChatChannelInitializer extends ChannelInitializer<NioSocketChannel> {

        private final List<ChannelHandler> handlers;

        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
            if (CollectionUtils.isNotEmpty(handlers)) {
                handlers.forEach(ch.pipeline()::addLast);
            }
        }
    }
}
