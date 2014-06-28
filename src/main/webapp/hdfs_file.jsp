<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("path", path);
	pageContext.setAttribute("basePath", basePath);
%>


<%
	if (request.getAttribute("file") == null) {
		request.getRequestDispatcher("/hdfs_browse/list.do").forward(
				request, response);
	}
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>Pangu hdfs file</title>

<link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">

<link href="<%=path%>/css/hdfs_file.css" rel="stylesheet">

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
		
		<div class="row" id="preview-div">
		
            <div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">预览</div>
					<div class="panel-body">
						${file.content }
					</div>
				</div>
			</div>
			
			<div class="col-md-12 text-right" id="btns-div">
				<button type="button" class="btn btn-default" id="back-btn">返回</button>
				<button type="button" class="btn btn-primary" id="download-btn">下载</button>
			</div>
			
		</div>

	</div>
	<!-- /.container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(init);
		function init() {
			$("#back-btn").click(function(){
				$("#nextPath").val($("#parent_path").val());
            	$("#enterFolderForm").submit();
			});
			$("#download-btn").click(function(){
				alert("暂不支持");
			});
		}
	</script>

	<form id="enterFolderForm" action="${path }/hdfs_browse/list.do" method="post">
        <input type="hidden" id="nextPath" name="path" value="">
    </form>

    <input type="hidden" id="current_path" value="${current_path}">
    <input type="hidden" id="parent_path" value="${parent_path}">
    
</body>
</html>