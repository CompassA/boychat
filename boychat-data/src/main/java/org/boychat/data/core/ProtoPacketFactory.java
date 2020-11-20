package org.boychat.data.core;

import com.google.protobuf.AbstractMessage;
import org.boychat.data.ChatPacket;

/**
 * create packet with magic number, length, version, serialization, id, msgType
 * @author tomato
 * Created on 2020.11.18
 */
public interface ProtoPacketFactory {

    /**
     * wrap the message into packet
     * @param message main body
     * @return packet with magic number, length, version, serialization, id, msgType
     */
    ChatPacket create(AbstractMessage message);
}
