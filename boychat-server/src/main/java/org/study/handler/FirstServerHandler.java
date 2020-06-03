package org.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.study.DateUtil;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author fanqie
 * @date 2020/6/3
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("%s: receive client msg [%s]\n",
                DateUtil.dateFormat(LocalDateTime.now()),
                ((ByteBuf) msg).toString(StandardCharsets.UTF_8));

        ctx.channel().writeAndFlush(
                ctx.alloc().buffer().writeBytes("hello client!".getBytes(StandardCharsets.UTF_8)));
    }


}
