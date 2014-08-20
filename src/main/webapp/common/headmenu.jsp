<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%    
String uri = request.getRequestURI();
if(uri.contains("hdfs_browse.jsp")){
	pageContext.setAttribute("page","hdfs_browse");    
}else if(uri.contains("hive_browse.jsp")){
	pageContext.setAttribute("page","hive_browse");    
}else if(uri.contains("cloud_test.jsp")){
	pageContext.setAttribute("page","cloud_test");     
}else if(uri.contains("cloud_job.jsp")){
	pageContext.setAttribute("page","cloud_job");   
}else if(uri.contains("today_repoter.jsp")){
	pageContext.setAttribute("page","today_repoter");   
}
%>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${path}/">盘古</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<!-- 
					<li class="active"><a href="${path}">盘古Pangu</a></li>
					 -->
				<li class="${page=='hdfs_browse'?'active':'' }"><a
					href="${path }/hdfs_browse.jsp">HDFS文件管理</a></li>
				<li class="${page=='hive_browse'?'active':'' }"><a
					href="${path }/hive_browse.jsp">HIVE管理</a></li>
				<li class="${page=='cloud_test'?'active':'' }"><a
					href="${path }/cloud_test.jsp">云测试</a></li>
				<li class="${page=='cloud_job'?'active':'' }"><a
					href="${path }/cloud_job.jsp">云调度</a></li>
				<li class="${page=='today_repoter'?'active':'' }"><a
					href="${path }/today_repoter.jsp">云统计</a></li>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
</div>


