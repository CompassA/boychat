package org.study.starter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.study.server.BoyChatReactorServer;

/**
 * @author fanqie
 * @date 2020/6/1
 */
@Configuration
@ComponentScan("org.study.handler")
public class BoyChatServerStarter {

    public static void main(String[] args) {
        new BoyChatReactorServer(BoyChatServerStarter.class, 9090).start();
    }

}
