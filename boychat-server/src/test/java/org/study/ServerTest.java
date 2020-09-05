package org.study;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fanqie
 * Created on 2020.09.04
 */
public class ServerTest {

    private static final String TEST_STR = "Hello World";
    private static final String HOST = "127.0.0.1";
    private static final int port = 12433;

    public static volatile String res = null;

    @Test
    public void handlerTests() {
        //start server
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {

                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new InHandlerOne())
                                        .addLast(new InHandlerTwo())
                                        .addLast(new InHandlerThree());
                            }

                        }
                ).bind(port);

        //start client
        new ThreadPoolExecutor(
                1,
                1,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new ThreadFactoryBuilder().setNameFormat("test-thread-pool-thread-id:%d").build(),
                new ThreadPoolExecutor.AbortPolicy()
        ).execute(() ->
            new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ClientHandler())
                    .connect(HOST, port)
                    .addListener(
                            future -> {
                                if (future.isSuccess()) {
                                    System.out.println("connected");
                                }
                            }
                    )
        );

        while (res == null) {
            continue;
        }
        Assert.assertEquals(res, TEST_STR + "_InHandlerOne" + "_InHandlerTwo" + "_InHandlerThree");
    }

    private static ByteBuf updateByteBuf(ByteBuf byteBuf, String message) {
        byteBuf.clear();
        byteBuf.writeBytes(message.getBytes());
        return byteBuf;
    }

    private static final class InHandlerOne extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);

            String newText = new String(bytes) + "_InHandlerOne";
            super.channelRead(ctx, updateByteBuf(byteBuf, newText));
        }
    }

    private static final class InHandlerTwo extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);

            String newText = new String(bytes) + "_InHandlerTwo";
            super.channelRead(ctx, updateByteBuf(byteBuf, newText));
        }
    }

    private static final class InHandlerThree extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);

            String newText = new String(bytes) + "_InHandlerThree";
            ctx.channel().writeAndFlush(updateByteBuf(byteBuf, newText));
        }
    }

    private static final class ClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            int length = TEST_STR.getBytes().length;
            ctx.channel().writeAndFlush(
                    ByteBufAllocator.DEFAULT
                            .buffer(length, length)
                            .writeBytes(TEST_STR.getBytes())
            );
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            res = new String(bytes);
        }
    }
}
