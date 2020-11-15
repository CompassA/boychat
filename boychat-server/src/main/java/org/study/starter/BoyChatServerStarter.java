package org.study.starter;

import org.study.server.BoyChatReactorServer;

/**
 * @author fanqie
 * @date 2020/6/1
 */
public class BoyChatServerStarter {

    public static void main(String[] args) {
        new BoyChatReactorServer(9090).start();
    }

}
