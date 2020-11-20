package org.study.encoder;

import com.google.protobuf.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.AllArgsConstructor;
import org.boychat.data.core.ProtoPacketFactory;

import java.util.List;

/**
 * @author tomato
 * Created on 2020.11.18
 */
@AllArgsConstructor
public class ProtoEncoder extends MessageToMessageEncoder<AbstractMessage> {

    private final ProtoPacketFactory protoPacketFactory;

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractMessage msg, List<Object> out) throws Exception {
        if (msg == null) {
            ctx.channel().close();
            return;
        }
        out.add(protoPacketFactory.create(msg));
    }
}
