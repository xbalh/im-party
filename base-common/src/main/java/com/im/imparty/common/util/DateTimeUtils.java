package com.im.imparty.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");

    public static String dateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTimeFormatter.format(dateTime);
    }

}
