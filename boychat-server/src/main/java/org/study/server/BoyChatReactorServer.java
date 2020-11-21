package org.study.server;

import com.google.common.collect.Lists;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.boychat.factory.CommonProtoPacketFactory;
import org.study.boychat.decoder.PacketDecoder;
import org.study.boychat.decoder.PacketSplitter;
import org.study.boychat.decoder.ProtoDecoder;
import org.study.boychat.encoder.PacketEncoder;
import org.study.boychat.encoder.ProtoEncoder;
import org.study.boychat.logger.TomatoLogger;
import org.study.handler.LoginHandler;
import org.study.handler.MessageHandler;

import java.util.List;

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

    private final List<ChannelHandler> handlers;

    public BoyChatReactorServer(int bindPort) {
        this.bindPort = bindPort;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.handlers = Lists.newArrayList(
                //decoder
                new PacketSplitter(),
                new PacketDecoder(),
                new ProtoDecoder(),

                //handler
                new LoginHandler(),
                new MessageHandler(),

                //encoder
                new PacketEncoder(),
                new ProtoEncoder(new CommonProtoPacketFactory())
        );
        this.serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new BoyChatReactorServer.BoyChatChannelInitializer(handlers));
    }

    public void start() {
        serverBootstrap.bind(bindPort);
        LOGGER.info("server started on port " + bindPort);
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
