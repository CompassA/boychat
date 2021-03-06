package org.study.boychat.common.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.boychat.constants.Constants;

/**
 * 解析报文长度
 * @author tomato
 * Created on 2020.11.18
 */
public class PacketSplitter extends LengthFieldBasedFrameDecoder {

    /**
     * 离长度字段多少字节
     */
    private static final int LENGTH_FIELD_OFFSET_BYTES = 21;

    /**
     * 长度字段本身多少字节
     */
    private static final int LENGTH_FIELD_BYTES = 4;
    
    public PacketSplitter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET_BYTES, LENGTH_FIELD_BYTES);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getInt(in.readerIndex()) != Constants.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}

