package org.study.boychat.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tomato
 * Created on 2020.11.21
 */
public final class ThreadPoolUtil {

    private ThreadPoolUtil() {
        throw new RuntimeException("permission denied");
    }

    public static ThreadPoolExecutor createSinglePool() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1),
                new ThreadFactoryBuilder().setNameFormat("console-thread-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
