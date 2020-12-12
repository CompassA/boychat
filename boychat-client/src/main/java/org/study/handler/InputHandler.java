package org.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;
import org.boychat.constants.Constants;
import org.boychat.data.ChatPacket;
import org.boychat.data.core.ProtoPacketFactory;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.MessageRequest;
import org.study.boychat.logger.TomatoLogger;
import org.study.boychat.utils.ReadWriteBufferUtil;
import org.study.client.data.ClientDataManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 处理输入输出
 * @author Tomato
 * Created on 2020.12.12
 */
public class InputHandler implements Runnable {

    private static final TomatoLogger LOGGER = TomatoLogger.getLogger(InputHandler.class);

    /**
     * 写死的结束标志
     */
    private static final String END_MARK = "bye";

    /**
     * 客户端持有的连接
     */
    private final Channel channel;

    /**
     * 把要发送的数据封装成包
     */
    private final ProtoPacketFactory protoPacketFactory;

    /**
     * 终端读入
     */
    private final BufferedReader consoleReader;

    /**
     * 客户端数据
     */
    private final ClientDataManager clientDataManager;

    public InputHandler(Channel channel, ProtoPacketFactory protoPacketFactory,
                        ClientDataManager clientDataManager) {
        this.channel = channel;
        this.protoPacketFactory = protoPacketFactory;
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        this.clientDataManager = clientDataManager;
    }

    @Override
    public void run() {
        int waitResponseCnt = 0;
        while (!Thread.interrupted()) {
            switch (clientDataManager.getClientState()) {
                case TO_LOGIN:
                    doLogin();
                    break;
                case CONNECTED:
                    doInput();
                    break;
                case WAIT_RESPONSE:
                default:
                    ++waitResponseCnt;
                    if (waitResponseCnt == 10) {
                        LOGGER.error("wait server response too long");
                        System.exit(0);
                    }
                    try {
                        Thread.sleep(waitResponseCnt * 1000);
                    } catch (InterruptedException e) {
                        LOGGER.error("sleep error", e);
                        System.exit(0);
                    }

            }
        }
    }

    private void doLogin() {
        System.out.print("please enter email:");
        final String email;
        try {
            email = consoleReader.readLine();
        } catch (IOException e) {
            LOGGER.error("read email failed", e);
            return;
        }
        System.out.print("please enter password: ");
        final String password;
        try {
            password = consoleReader.readLine();
        } catch (IOException e) {
            LOGGER.error("read password failed", e);
            return;
        }
        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            LOGGER.error("invalid email\\password");
            return;
        }
        ChatPacket packet = protoPacketFactory.create(
                LoginRequest.newBuilder()
                        .setEmail(email)
                        .setPassword(password)
                        .build()
        );
        int bufferLength = packet.getLength() + Constants.HEADER_LENGTH;
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bufferLength, bufferLength);
        ReadWriteBufferUtil.write(buffer, packet);
        this.channel.writeAndFlush(buffer);
        clientDataManager.processAfterLogin(email);
    }

    private void doInput() {
        String oneLine = StringUtils.EMPTY;
        try {
            oneLine = consoleReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oneLine.equals(END_MARK)) {
            LOGGER.info("client quit");
            channel.close().addListener(future -> {
                if (future.isSuccess()) {
                    System.exit(0);
                } else {
                    channel.close().addListener(f -> {
                        if (f.isSuccess()) {
                            System.exit(0);
                        }
                    });
                }
            });
        }
        ChatPacket packet = protoPacketFactory.create(
                MessageRequest.newBuilder()
                        .setDesEmail("TODO")
                        .setSrcEmail(clientDataManager.getEmail())
                        .setMessage(oneLine)
                        .build());
        int bufferLen = packet.getLength() + Constants.HEADER_LENGTH;
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(bufferLen, bufferLen);
        ReadWriteBufferUtil.write(byteBuf, packet);
        channel.writeAndFlush(byteBuf);
    }
}
