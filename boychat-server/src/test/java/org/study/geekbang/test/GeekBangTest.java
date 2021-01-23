package org.study.geekbang.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tomato
 * Created on 2021.01.23
 */
public class GeekBangTest {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 2435;

    @Test
    public void test() throws InterruptedException {
        // ===============init
        CountDownLatch countDownLatch = new CountDownLatch(2);
        MessageHandler.LATCH = countDownLatch;
        ClientMessageHandler.LATCH = countDownLatch;
        ServerBootstrap serverBootstrap = initServer();
        Bootstrap bootstrap = initClient();

        //===============start
        long[] beginTime = new long[1];
        serverBootstrap.bind(PORT);
        bootstrap.connect(IP, PORT)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        beginTime[0] = System.nanoTime();
                        Channel channel = ((ChannelFuture) future).channel();
                        for (int i = 0; i < 100000; ++i) {
                            byte[] body = (i + ":client message").getBytes(StandardCharsets.UTF_8);
                            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(body.length + 8, body.length + 8);
                            byteBuf.writeInt(body.length + 4);
                            byteBuf.writeInt(i);
                            byteBuf.writeBytes(body);
                            channel.write(byteBuf);

                            System.out.printf("client: sent message %d\n", i);
                        }
                        channel.flush();
                    }
                });

        countDownLatch.await();

        long endTime = System.nanoTime();
        double costTime = (endTime - beginTime[0])/1000000000.0;
        System.out.printf("cost time: %f\n", costTime);
    }

    private Bootstrap initClient() {
        return new Bootstrap()
                .group(new NioEventLoopGroup(1))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                .addLast(new LengthDecoder("client"))
                                .addLast(new MessageDecoder("client"))
                                //.addLast(new ClientMessageHandler());
                                .addLast(ClientMessageHandler.INSTANCE);
                    }
                });

    }

    private ServerBootstrap initServer() {
        return new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(1))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline()
                                .addLast(new LengthDecoder("server"))
                                .addLast(new MessageDecoder("server"))
                                //.addLast(new MessageHandler())
                                .addLast(MessageHandler.INSTANCE)
                                .addLast(new MessageEncoder());
                    }
                });
    }

    private static class Message {
        public int length;
        public int id;
        public byte[] body;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Message message = (Message) o;
            return message.id != this.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    private static class LengthDecoder extends LengthFieldBasedFrameDecoder {

        private final String name;

        public LengthDecoder(String name) {
            super(Integer.MAX_VALUE, 0, 4);
            this.name = name;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
        }
    }

    private static class MessageDecoder extends ByteToMessageDecoder {

        private final String name;

        private MessageDecoder(String name) {
            this.name = name;
        }

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
                              List<Object> list) throws Exception {
            Message message = new Message();
            message.length = byteBuf.readInt();
            message.id = byteBuf.readInt();
            message.body = new byte[message.length - 4];
            byteBuf.readBytes(message.body);
            list.add(message);
        }
    }

    @ChannelHandler.Sharable
    private static class MessageHandler extends SimpleChannelInboundHandler<Message> {

        public static CountDownLatch LATCH;

        public static MessageHandler INSTANCE = new MessageHandler();

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                    Message message) throws Exception {
            Message response = new Message();
            response.id = message.id;
            byte[] body = String.format("回复第%d条消息", message.id).getBytes(StandardCharsets.UTF_8);
            response.body = body;
            response.length = body.length + 4;

            channelHandlerContext.channel().write(response);
            System.out.printf("server: 回复第%d条消息\n", response.id);

            if (response.id == 99999 && LATCH != null) {
                channelHandlerContext.channel().flush();
                LATCH.countDown();
            }
        }
    }

    private static class MessageEncoder extends MessageToByteEncoder<Message> {

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
            byteBuf.writeInt(message.length);
            byteBuf.writeInt(message.id);
            byteBuf.writeBytes(message.body);
        }
    }

//    private static class ServerGlobalManager {
//
//        public static final AtomicInteger CNT = new AtomicInteger(0);
//
//        public static final BlockingQueue<Message> MESSAGE_BLOCKING_DEQUE = new ArrayBlockingQueue<>(5000);
//
//    }
//
    @ChannelHandler.Sharable
    private static class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {

        public static CountDownLatch LATCH;

        public static ClientMessageHandler INSTANCE = new ClientMessageHandler();

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

            System.out.printf("client: receive response %d: %s\n",
                    message.id, new String(message.body, StandardCharsets.UTF_8));
            //ClientGlobalManager.RECEIVED_MESSAGE.put(message.id, message);

            if (message.id == 99999 && LATCH != null) {
                LATCH.countDown();
            }
        }
    }

    private static class ClientGlobalManager {
        public static final AtomicInteger CNT = new AtomicInteger(0);

        public static final ConcurrentMap<Integer, Message> RECEIVED_MESSAGE = new ConcurrentHashMap<>();
    }
}
