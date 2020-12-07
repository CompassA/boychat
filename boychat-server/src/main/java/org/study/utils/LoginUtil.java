package org.study.utils;


import io.netty.channel.Channel;
import io.netty.util.Attribute;
import org.study.boychat.data.Attributes;

/**
 * @author Tomato
 * Created on 2020.12.07
 */
public final class LoginUtil {

    private LoginUtil() {
        throw new RuntimeException("permission denied");
    }

    public static void markLogin(Channel channel) {
        channel.attr(Attributes.LOGIN_MARK).set(true);
    }

    public static boolean isLogin(Channel channel) {
        Attribute<Boolean> loginMark = channel.attr(Attributes.LOGIN_MARK);
        return loginMark != null && loginMark.get();
    }
}
