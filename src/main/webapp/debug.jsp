<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="org.quartz.JobKey" %>
<%@page import="org.quartz.Scheduler" %>
<%@page import="org.quartz.SchedulerFactory" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="static org.quartz.impl.matchers.GroupMatcher.*" %>
<%@page import="org.yws.pangu.service.JobManager" %>
<%@page import="org.yws.pangu.utils.JobExecutionMemoryHelper" %>
<%@page import="java.util.Set" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Insert title here</title>
</head>
<body>
<%
    ServletContext context = request.getSession().getServletContext();
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

    SchedulerFactory schedulerFactory = (SchedulerFactory) ctx.getBean("schedulerFactory");
    JobManager jobManager = (JobManager) ctx.getBean(JobManager.class);

    Scheduler scheduler = schedulerFactory.getScheduler();

    Set<JobKey> keys = scheduler.getJobKeys(anyJobGroup());

    out.println("<br/>自动运行任务： <br/>");
    for (JobKey key : keys) {
        out.print("组名: " + key.getGroup() + ", 任务ID:" + key.getName());
        out.print("<br/>");
    }


    out.println("<br/>任务依赖： <br/>");
    for (String key : jobManager.getDependenciesMap().keySet()) {
        out.print("Job ID: " + key);
        out.print("依赖于: ");

        for (String j : jobManager.getDependenciesMap().get(key)) {
            out.print(j + "&nbsp;&nbsp;");
        }
        out.print("<br/>");
    }

    out.println("<br/>Running Jobs History ID: <br/>");
    for (Long key : JobExecutionMemoryHelper.jobLogMemoryHelper.keySet()) {
        out.print("Job ID: " + key);
        out.print("<br/>");
    }
%>

</body>
</html>  