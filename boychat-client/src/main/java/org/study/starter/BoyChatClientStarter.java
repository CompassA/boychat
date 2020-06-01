package org.study.starter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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

                    }
                });
        connect(bootstrap, "127.0.0.1", 9090, 3);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retryTimes) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("connection succeeded!");
            } else if (retryTimes > 0) {
                System.out.println("connection failed, client will retry in 5 seconds!");
                bootstrap.config().group().schedule(
                        () -> connect(bootstrap, host, port, retryTimes - 1),
                        5, TimeUnit.SECONDS);
            } else {
                System.out.println("connection failed!");
            }
        });
    }
}
