package org.study.boychat.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.study.boychat.utils.ReadWriteBufferUtil;

import java.util.List;

/**
 * @author tomato
 * Created on 2020.11.19
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(ReadWriteBufferUtil.read(in));
    }
}