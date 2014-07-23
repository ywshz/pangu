package org.yws.pangu.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class DateRender {

	public static String render(String content) {
		VelocityContext context = new VelocityContext();
		context.put("zdt", new PanguDateTool());
		Pattern pt = Pattern.compile("\\$\\{zdt.*\\}");
		Matcher matcher = pt.matcher(content);
		while (matcher.find()) {
			String m = content.substring(matcher.start(), matcher.end());
			StringWriter sw = new StringWriter();
			try {
				Velocity.evaluate(context, sw, "", m);
			} catch (Exception e) {
				return null;
			}
			content = content.replace(m, sw.toString());
		}
		return content;
	}

	public static void main(String[] args) throws ParseErrorException, MethodInvocationException,
			ResourceNotFoundException, IOException {
		
		System.out.println(DateRender.render("abc${zdt.addDay(-1).format(\"yyyyMMdd\")} ${zdt.addDay(1).format(\"yyyyMMdd\")}"));
		
//		VelocityContext context = new VelocityContext();
//		context.put("zdt", new PanguDateTool());
//		String s = "abc${zdt.addDay(-1).format(\"yyyyMMdd\")} ${zdt.addDay(1).format(\"yyyyMMdd\")}";
//		Pattern pt = Pattern.compile("\\$\\{zdt.*\\}");
//		Matcher matcher = pt.matcher(s);
//		while (matcher.find()) {
//			String m = s.substring(matcher.start(), matcher.end());
//			System.out.println(m);
//			StringWriter sw = new StringWriter();
//			Velocity.evaluate(context, sw, "", m);
//			System.out.println(sw.toString());
//			s = s.replace(m, sw.toString());
//		}
//		System.out.println("result:" + s);
	}
}
