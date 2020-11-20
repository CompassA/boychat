package org.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.boychat.constants.Constants;
import org.boychat.enums.MsgType;
import org.boychat.enums.SerializationEnum;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.LoginResponse;
import org.study.boychat.data.MessageResponse;
import org.study.constans.Attributes;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fanqie
 * @date 2020/6/3
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //create request
        LoginRequest loginMsg = LoginRequest.newBuilder()
                .setAccount(UUID.randomUUID().toString())
                .setPassword(UUID.randomUUID().toString())
                .build();
        byte[] loginMsgBytes = loginMsg.toByteArray();
        int bufferLength = loginMsgBytes.length + Constants.HEADER_LENGTH;
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(bufferLength, bufferLength)
                .writeInt(Constants.MAGIC_NUMBER)
                .writeInt(Constants.VERSION)
                .writeByte(SerializationEnum.PROTO.getId())
                .writeLong(ID_GENERATOR.getAndIncrement())
                .writeInt(MsgType.LOGIN.getTypeId())
                .writeInt(loginMsgBytes.length)
                .writeBytes(loginMsgBytes);
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //create byte buf
        ByteBuf in = (ByteBuf) msg;
        int magicNumber = in.readInt();
        int version = in.readInt();
        byte serialization = in.readByte();
        long id = in.readLong();
        int type = in.readInt();
        int length = in.readInt();
        byte[] body = new byte[length];
        in.readBytes(body);

        //deserialize
        switch (MsgType.getById(type)) {
            case LOGIN:
                LoginResponse loginResponse = LoginResponse.parseFrom(body);
                if (loginResponse.getSuccess()) {
                    ctx.channel().attr(Attributes.LOGIN_MARK).set(true);
                    System.out.println("登录成功！");
                } else {
                    System.out.println("登录失败！");
                }
                break;
            case MSG:
                MessageResponse messageResponse = MessageResponse.parseFrom(body);
                System.out.println("收到服务端响应:" + messageResponse.getMessage());
                break;
            default:
        }
    }
}
