package org.yws.pangu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ywszjut on 14-6-22.
 */
public class DateUtils {

	private static SimpleDateFormat format = new SimpleDateFormat();

	public static String format(long time) {
		return format(time, "yyyy-MM-dd HH:mm:ss");
	}

	public static String format(long time, String pattern) {
		format.applyPattern(pattern);
		return format.format(new Date(time));
	}

	public static String getNDaysTimeByPattern(int days, String pattern) {
		format.applyPattern(pattern);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		return format.format(cal.getTime());
	}

	public static Date getNDaysBeginTime(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static void main(String[] args) {
		String s = DateUtils.format(System.currentTimeMillis());
		System.out.println(s);
		
		System.out.println(DateUtils.getNDaysBeginTime(0));
		System.out.println(DateUtils.getNDaysBeginTime(1));
		
	}
}
