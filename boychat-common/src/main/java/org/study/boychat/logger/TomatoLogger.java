package org.study.boychat.logger;

import jdk.internal.instrumentation.Logger;
import org.study.boychat.utils.DateUtil;

import java.io.PrintStream;
import java.time.LocalDateTime;

/**
 * @author tomato
 * Created on 2020.11.21
 */
public class TomatoLogger implements Logger {

    private static final PrintStream PRINT_STREAM = System.out;

    public static TomatoLogger getLogger(Class<?> clazz) {
        return new TomatoLogger(clazz);
    }

    private final Class<?> clazz;

    private TomatoLogger(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void error(String s) {
        PRINT_STREAM.println(commonPrefix(LogType.ERROR) + s);
    }

    @Override
    public void warn(String s) {

    }

    @Override
    public void info(String s) {
        PRINT_STREAM.println(commonPrefix(LogType.INFO) + s);
    }

    @Override
    public void debug(String s) {

    }

    @Override
    public void trace(String s) {
    }

    @Override
    public void error(String s, Throwable throwable) {
        PRINT_STREAM.println(commonPrefix(LogType.ERROR) + s);
        PRINT_STREAM.println(throwable);
    }

    private String commonPrefix(LogType logType) {
        return String.format("[%s][%s][%s]: ",
                DateUtil.dateFormat(LocalDateTime.now()), clazz.getName(), logType);
    }

    private enum LogType {
        /**
         * Log types
         */
        INFO("info"),
        DEBUG("debug"),
        ERROR("error"),
        WARN("warn"),
        ;

        private final String type;

        LogType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}