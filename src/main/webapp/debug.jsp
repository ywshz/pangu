<?xml version="1.0" encoding="UTF-8" ?>  
<%@ page language="java" contentType="text/html; charset=UTF-8"  
    pageEncoding="UTF-8"%>  
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>  
<%@page import="org.springframework.context.ApplicationContext"%>  
<%@page import="org.quartz.SchedulerFactory"%>  
<%@page import="org.quartz.Scheduler"%>  
<%@page import="org.quartz.SchedulerException"%>  
<%@page import="static org.quartz.impl.matchers.GroupMatcher.*"%>  
<%@page import="java.util.Set"%>  
<%@page import="org.quartz.JobKey"%>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />  
<title>Insert title here</title>  
</head>  
<body>  
<%  
ServletContext context = request.getSession().getServletContext();  
ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);  

SchedulerFactory schedulerFactory = (SchedulerFactory)ctx.getBean("schedulerFactory");

Scheduler scheduler = schedulerFactory.getScheduler();

Set<JobKey> keys = scheduler.getJobKeys(anyJobGroup());

out.print("Schedule Jobs: <br/>");
for(JobKey key : keys ){
	out.print("Group Name: "+ key.getGroup()+", Job ID:"+key.getName());
	out.print("<br/>");
}
%>  
</body>  
</html>  