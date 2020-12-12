package org.study.starter;

import org.study.client.BoyChatClient;

/**
 * @author fanqie
 * @date 2020/6/1
 */
public class BoyChatClientStarter {

    public static void main(String[] args) {
        BoyChatClient client = new BoyChatClient("127.0.0.1", 9090);
        client.connect(3);
    }
}
