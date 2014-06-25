package org.yws.pangu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ywszjut on 14-6-22.
 */
public class DateUtils {

    private static SimpleDateFormat format = new SimpleDateFormat();

    public static String format(long time){
        return format(time,"yyyy-MM-dd HH:mm:ss");
    }

    public static String format(long time, String pattern){
        format.applyPattern(pattern);
        return format.format(new Date(time));
    }

    public static void main(String[] args) {
    String s =    DateUtils.format(System.currentTimeMillis());
        System.out.println(s);
    }
}
