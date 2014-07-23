package org.yws.pangu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期工具类 用于界面配置项的动态替换
 *
 */
public class PanguDateTool {

	private Calendar calendar = Calendar.getInstance();

	public PanguDateTool addDay(int amount) {
		calendar.add(Calendar.DAY_OF_YEAR, amount);
		return this;
	}

	public PanguDateTool add(int field, int amount) {
		calendar.add(field, amount);
		return this;
	}

	public String format(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(calendar.getTime());
	}

	public static void main(String[] args) {
		String v = new PanguDateTool().add(Calendar.DAY_OF_MONTH, -1).format("yyyy-MM-dd");
		System.out.println(v);
	}
}
