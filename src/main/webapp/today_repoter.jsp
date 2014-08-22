<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.context.ApplicationContext"%>
<%@ page import="org.yws.pangu.service.impl.JobServiceImpl"%>
<%@ page import="org.yws.pangu.domain.JobBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.TreeSet"%>
<%@ page import="java.util.Iterator"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":"
	+ request.getServerPort() + path + "/";
	pageContext.setAttribute("path", path);
	pageContext.setAttribute("basePath", basePath);
%>

<%
	ServletContext context = request.getSession().getServletContext();
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	JobServiceImpl js = ctx.getBean(JobServiceImpl.class);
	int[] sf = js.getSuccessFailedJobsByDate(0);

	Map<String, int[]> m = js.getSuccessFailedJobsInNDays(5);

	int size = m.size();
	Set<String> s = new TreeSet<String>(m.keySet());
	Iterator<String> it = s.iterator();
	
	String[] catalogs = new String[size];
	int[] successes = new int[size];
	int[] faileds = new int[size];
	
	int index=0;
	while (it.hasNext()) {
		String key = it.next();
		int[] sf2 = m.get(key);

		catalogs[index]="'"+key+"'";
		successes[index]=sf2[0];
		faileds[index]=sf2[1];
		index++;
	}
	
	//////////
	
	List<String> job16 = js.jobCostTimeInNDays(16, 30);
	List<String> job23 = js.jobCostTimeInNDays(23, 30);
	List<String> job24 = js.jobCostTimeInNDays(24, 30);
%>


<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>Pangu</title>

<link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">

<link href="${path }/css/index.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="${path }/bootstrap/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

<script type="text/javascript">
	var c = new Array();
	var s = new Array();
	var f = new Array();
<%for(int i=0;i<size;i++){%>
	c.push(
<%=catalogs[i]%>
	);
	s.push(
<%=successes[i]%>
	);
	f.push(
<%=faileds[i]%>
	);
<%}%>
	var job16 = new Array();
<%for(String j : job16){%>
	job16.push(
<%=j%>
	);
<%}%>
	var job23 = new Array();
<%for(String j : job23){%>
	job23.push(
<%=j%>
	);
<%}%>
	var job24 = new Array();
<%for(String j : job24){%>
	job24.push(
<%=j%>
	);
<%}%>
	
</script>

</head>

<body>
	<%@ include file="common/headmenu.jsp"%>

	<div class="container">
		<div id="sf-chart"></div>
		<hr />
		<div id="job-costtime-chart"></div>
	</div>
	<!-- /.container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${path }/highcharts/js/highcharts.js"></script>
	<script src="${path }/js/today_repoter.js"></script>
</body>
</html>

