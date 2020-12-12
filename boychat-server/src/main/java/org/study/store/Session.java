package org.study.store;

import io.netty.channel.Channel;

/**
 * @author Tomato
 * Created on 2020.12.10
 */
public interface Session {

    String getToken();

    String getEmail();

    String getUserName();

    String getUserPassword();

    Channel getChannel();

    void setToken(String token);
}
