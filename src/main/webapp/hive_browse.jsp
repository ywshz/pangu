<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%    
String path = request.getContextPath();    
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
pageContext.setAttribute("path",path);    
pageContext.setAttribute("basePath",basePath);    
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>Pangu HIVE browse</title>

<link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${path }/css/hdfs_browse.css" rel="stylesheet">

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
		
		<div class="row action-div">
			
			<form class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-1 control-label">数据库:</label>
						<div class="col-sm-3">
							<select class="form-control" id="db-select">
							</select>
						</div>
						<div class="col-sm-2">
							<input type="button" class="btn btn-default" id="db-selected-btn" value="确定">
						</div>
					</div>
			</form>

		</div>

		<div id="table-info-div">
			<div class="row">
			<div class="col-md-3" id="table-group-div">
				当前选择数据库:<label id="viewing-db-label">1</label>
				<div class="list-group" id="table-list-div">
				</div>
			</div>
			<div class="col-md-9" id="table-detail-div">
				<table class="table table-condensed table-hover table-bordered">
					<tbody>
						<tr>
							<td>表名</td>
							<td id="table-name-td"></td>
							<td>类型</td>
							<td id="table-type-td"></td>
						</tr>
						<tr>
							<td>拥有者</td>
							<td id="table-owner-td"></td>
							<td>创建时间</td>
							<td id="table-createtime-td"></td>
						</tr>
						<tr>
							<td>输入格式</td>
							<td colspan="3" id="table-inputformat-td"></td>
						</tr>
						<tr>
							<td>输出格式</td>
							<td colspan="3" id="table-outformat-td"></td>
						</tr>
						<tr>
							<td>路径</td>
							<td colspan="3" id="table-localtion-td"></td>
						</tr>
					 </tbody>
				</table>
				
				列信息:
				<table class="table table-condensed table-hover table-bordered">
					<thead>
				        <tr>
				          <th>名称</th>
				          <th>类型</th>
				          <th>注释</th>
				        </tr>
				    </thead>
					<tbody id="column-tbody">
					 </tbody>
				</table>
				
				分区信息:
				<table class="table table-condensed table-hover table-bordered">
					<thead>
				        <tr>
				          <th>名称</th>
				          <th>类型</th>
				          <th>注释</th>
				        </tr>
				    </thead>
					<tbody id="partition-tbody">
					 </tbody>
				</table>
			</div>
		</div>
	</div>
	</div>
	<!-- /.container -->

	<input type="hidden" id="select-db-input" value="">
	<input type="hidden" id="select-table-input" value="">
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
        	$("#table-group-div").hide();
        	$("#table-detail-div").hide();
        	
        	$.post("${path }/hive/databases.do",function(res){
        		$("#db-select").append("<option value='default'>==先选择数据库==</option>");
        		for(var i in res){
	        		$("#db-select").append("<option value='"+res[i]+"'>"+res[i]+"</option>");
                } 
        	});
        	
        	$("#db-selected-btn").click(function(){
        		$("#table-group-div").show();
        		$("#table-detail-div").hide();
        		$("#viewing-db-label").html($("#db-select").val());
        		
        		$("#select-db-input").val($("#db-select").val());
        		$.post("${path }/hive/tables.do",{db:$("#db-select").val()},function(res){
        			$("#table-list-div").html("");
            		for(var i in res){
    	        		$("#table-list-div").append('<a href="javascript:void(0);" class="list-group-item">'+res[i]+'</a>');
                    } 
            		
	        		$(".list-group-item").click(function(){
	        			$(".list-group-item").removeClass("active");
	        			$(this).addClass("active");
	        			
	        			$.post("${path }/hive/table.do",{db:$("#select-db-input").val(),table:this.text},function(res){
	        				$("#table-detail-div").show();
	        				
	        				$("#table-name-td").html(res.tableName);
	        				$("#table-type-td").html(res.tableType);
	        				$("#table-owner-td").html(res.owner);
	        				$("#table-createtime-td").html(res.createTime);
	        				$("#table-inputformat-td").html(res.inputFormat);
	        				$("#table-outformat-td").html(res.outputFormat);
	        				$("#table-localtion-td").html(res.location);
	        				
	        				$("#column-tbody").html("");
	        				for(var i in res.columns){
	        					var line = "<tr><td>"+res.columns[i].name+"</td><td>"+res.columns[i].type+"</td><td>"+res.columns[i].comment+"</td></tr>";
	        					$("#column-tbody").append(line);
	        				}
	        				
	        				$("#partition-tbodybody").html("");
							for(var i in res.partitions){
								var line = "<tr><td>"+res.partitions[i].name+"</td><td>"+res.partitions[i].type+"</td><td>"+res.partitions[i].comment+"</td></tr>";
								$("#partition-tbody").append(line);
	        				}
	        			});
	        		});
            	});
        		
        	});
        });
    </script>
</body>
</html>