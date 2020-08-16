package org.study;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;
import org.study.boychat.data.LoginRequest;
import org.study.boychat.data.Message;

/**
 * @author fanqie
 * Created on 2020.08.15
 */
public class MessageTest {

    @Test
    public void encodeDecodeTest() throws InvalidProtocolBufferException {
        //build login msg
        String account = "user person";
        String password = "1234";
        LoginRequest loginMsg = LoginRequest.newBuilder()
                .setAccount(account)
                .setPassword(password)
                .build();
        byte[] body = loginMsg.toByteArray();

        //build msg
        Message msg = Message.newBuilder()
                .setMagicNumber(0xababcdef)
                .setVersion(1)
                .setType(1)
                .setLength(body.length)
                .setBody(ByteString.copyFrom(body))
                .build();

        byte[] bytes = msg.toByteArray();

        Message msgDecode = Message.parseFrom(bytes);
        Assert.assertEquals(msgDecode, msg);

        byte[] body_ = msgDecode.getBody().toByteArray();
        Assert.assertArrayEquals(body_, body);

        Assert.assertEquals(loginMsg, LoginRequest.parseFrom(body_));
    }
}
