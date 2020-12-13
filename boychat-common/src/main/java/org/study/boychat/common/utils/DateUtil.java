package org.study.boychat.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fanqie
 * @date 2020/6/3
 */
public final class DateUtil {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() { throw new RuntimeException("illegal call!"); }

    public static String dateFormat(LocalDateTime date) {
        return dateFormat(date, DEFAULT_FORMAT);
    }

    public static String dateFormat(LocalDateTime date, String format) {
        return DateTimeFormatter.ofPattern(format).format(date);
    }
}
