<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
				<li class="${page=='topics'?active:'' }"><a
					href="${path }/hdfs_browse.jsp">HDFS文件管理</a></li>
				<li class="${page=='topics'?active:'' }"><a
					href="${path }/developing.html">云测试</a></li>
				<li class="${page=='topics'?active:'' }"><a
					href="${path }/developing.html">云调度</a></li>
				<li class="${page=='topics'?active:'' }"><a
					href="${path }/developing.html">云统计</a></li>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
</div>


