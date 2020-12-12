package org.study.store.impl;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Setter;
import org.study.store.Session;

/**
 * @author Tomato
 * Created on 2020.12.10
 */
@Builder
public class TomatoSession implements Session {

    @Setter
    private String token;

    private final String email;

    private final String userName;

    private final String userPassword;

    private final Channel channel;

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public String getUserPassword() {
        return this.userPassword;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }
}
