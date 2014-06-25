<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    pageContext.setAttribute("path", path);
    pageContext.setAttribute("basePath", basePath);
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>Kafka Web Interface</title>

<link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">

<link href="${path }/css/topics.css" rel="stylesheet">

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="${path }/bootstrap/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<%@ include file="common/headmenu.jsp"%>

	<div class="container">

		<div class="row">
			<div class="col-md-12">
				<div class="div-result">
					<table class="table">
						<thead>
							<tr>
								<th>Job Name</th>
								<th>Create Time</th>
								<th>Last Run Time</th>
								<th>时间表达式</th>
								<th>当前状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="job" items="${jobList }">
								<tr <c:if test="${job.jobStatus=='Running' }">class="success"</c:if>>
									<td>${job.jobName }</td>
									<td>${job.createTime }</td>
									<td>${job.lastRunTime }</td>
									<td>${job.cron }</td>
									<td>${job.jobStatus }</td>
									<td>
										<button type="button" class="btn btn-default btn-xs stopBtn" data="${job.id }">停止</button>
										<button type="button" class="btn btn-default btn-xs startBtn" data="${job.id }">立即运行</button>
										<button type="button" class="btn btn-default btn-xs">编辑</button>
										<button type="button" class="btn btn-default btn-xs">删除</button>
										<button type="button" class="btn btn-default btn-xs">查看日志</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>
	<!-- /.container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${path }/js/jobstatus.js"></script>
</body>
</html>
