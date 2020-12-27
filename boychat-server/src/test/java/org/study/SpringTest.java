package org.study;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.study.handler.LoginHandler;

/**
 * @author Tomato
 * Created on 2020.12.27
 */
public class SpringTest {

    @Test
    public void annotationApplicationContext() {
        ApplicationContext context = new AnnotationConfigApplicationContext("org.study.handler");
        Assert.assertNotNull(context.getBeanProvider(LoginHandler.class).getIfAvailable());
        Assert.assertNotNull(context.getBeanProvider(LoginHandler.class).getIfAvailable());
    }
}
