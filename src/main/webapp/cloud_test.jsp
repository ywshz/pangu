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

<title>盘古 -- 云测试</title>

<link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">

<link href="<%=path %>/css/cloud_test.css" rel="stylesheet">

<link href="${path }/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	
<style type="text/css">
div#rMenu {position:fixed; visibility:hidden; top:0;z-index:1000}
</style>
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
        
        	<div class="col-md-2">
        		<div class="panel panel-default">
					<div class="panel-body">
	        			<div class="zTreeDemoBackground left">

			        		<ul id="tree" class="ztree">
							 </ul>
	        			</div>
		        	</div>
		        	<div id="rMenu" class="btn-group-vertical">
						<button type="button" id="addFolderBtn" class="btn btn-default">创建文件夹</button>
					    <button type="button" id="addFileBtn" class="btn btn-default">创建文件</button>
					    <button type="button" id="addHiveBtn" class="btn btn-default">创建Hive</button>
					    <button type="button" id="addShellBtn" class="btn btn-default">创建Shell</button>
					    <button type="button" id="openBtn" class="btn btn-default">打开</button>
					    <button type="button" id="deleteBtn" class="btn btn-default">删除</button>
					</div>
		        </div>
	        </div>
	        
	        <div class="col-md-10">
	        	<div class="panel panel-default">
					<div class="panel-body">
					
						<div>
							<div id="run-toolbar-div" class="row">
								<div class="col-md-6">
									<button type="button" class="btn btn-default" id="save-content-btn">
										<span class="glyphicon glyphicon-floppy-disk"></span>保存
									</button>
									
									<button type="button" class="btn btn-primary" id="run-btn">
										 <span class="glyphicon glyphicon-play"></span>运行
									</button>
									
									<button type="button" class="btn btn-default" id="run-select-btn">
										<span class="glyphicon glyphicon-ok"></span>运行选中
									</button>
									
									<button type="button" class="btn btn-default" id="public-var-btn">
										<span class="glyphicon glyphicon-wrench"></span>公共变量
									</button>
								</div>
								<div class="col-md-6 text-right">
									<samp id="op-info"></samp>
								</div>
							</div>
							<textarea id="editbox" class="form-control" rows="16"></textarea>
						</div>
						<div class="panel panel-default console-panel">
							<div class="panel-heading">运行日志</div>
							<div class="panel-body">
								<div id="console-div" style='height:200px;overflow: auto;'>
									<p id="log-p"></p>
								</div>
							</div>
						</div>
					</div>
				</div>
	        </div>
	        
        </div>

	</div>
	<!-- /.container -->
	
	<input type="hidden" id="editing-file-input" value=""/>
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
    
	<script type="text/javascript" src="${path }/js/jquery.ztree.core-3.5.js"></script>
    
    
    <script type="text/javascript">
	
        $(document).ready(init);
        
        function init(){
        	$(document).ready(function(){
    			$.fn.zTree.init($("#tree"), {
    				async: {
    					enable: true,
    					url:"/files/list.do",
    					autoParam:["id"]
    				},
    				callback: {
    					onRightClick: OnRightClick,
    					onClick: OnLeftClick
    				}
    			});
    			
    			zTree = $.fn.zTree.getZTreeObj("tree");
    			rMenu = $("#rMenu");
    			hideRMenu();
    		});
        	
        	resetToolBar();
        	$("#run-btn").click(runBtnClick);
        	$("#save-content-btn").click(saveContentBtnClick);
        	$("#editbox").keydown(function(){
        		$("#op-info").text("已修改未保存");
        	});
        }
        
        function resetToolBar(){
        	$("#save-content-btn").attr("disabled","disabled");
        	$("#run-btn").attr("disabled","disabled");
        	$("#run-select-btn").attr("disabled","disabled");
        	$("#editbox").attr("disabled","disabled");
        }
        
        function OnLeftClick(event, treeId, treeNode) {
        	if(!treeNode.folder){
        		$.post("/files/content.do",{fileId:treeNode.id},function(data){
        			$("#editbox").val(data);
        			$("#op-info").text("已打开: "+treeNode.name);
        			$("#editing-file-input").val(treeNode.id);
        			$("#editbox").removeAttr("disabled");
        			$("#save-content-btn").removeAttr("disabled");
                	$("#run-btn").removeAttr("disabled");
                	$("#run-select-btn").removeAttr("disabled");
        		});
        	}else{
        		resetToolBar();
        		$("#op-info").text("");
        		$("#editbox").val("");
        		$("#editing-file-input").val("");
        	}
        }
        
        function OnRightClick(event, treeId, treeNode) {
        	if(treeNode==null) return;
        	
        	showRMenu(treeNode.folder, event.clientX, event.clientY);
		}

		function showRMenu(isFolder, x, y) {
			$("#rMenu button").show();
			if (isFolder) {
				$("#openBtn").hide();
			}else{
			    $("#addFolderBtn").hide();
			    $("#addFileBtn").hide();
			    $("#addHiveBtn").hide();
			    $("#addFileBtn").hide();
			    $("#addShellBtn").hide();
			}
			
			rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});
			
			$("body").bind("mousedown", onBodyMouseDown);
		}
		function hideRMenu() {
			if (rMenu) rMenu.css({"visibility": "hidden"});
			$("body").unbind("mousedown", onBodyMouseDown);
		}
		function onBodyMouseDown(event){
			if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
				rMenu.css({"visibility" : "hidden"});
			}
		}
		
        function runBtnClick(){
        	$.post("/files/run.do",{content:$("#editbox").val(),fileId:$("#editing-file-input").val()},function(res){
        		if(res){
        			var timeId = setInterval(function(){
                	    $.post("/files/getlog.do",{fileId:$("#editing-file-input").val()},function(res){
                	    	if(res.status=="END"){
                	    		clearTimeout(timeId);
                	    	}
                	    	var cr = $("#log-p").html();
                	    	var nr = res.log.replace(/\n/g, "<br>");
                	    	if(cr!=nr){
                	    		$("#log-p").html(nr);
        		        	    document.getElementById('console-div').scrollTop=document.getElementById('console-div').scrollHeight;
                	    	}
                	    });        
                	    
                	    
                	},1000);
        		}else{
        			alert("运行失败");
        		}
        	});
        }
        
        function saveContentBtnClick(){
        	$.post("/files/updatecontent.do",{content:$("#editbox").val(),fileId:$("#editing-file-input").val()},function(res){
        		if(res){
	        		$("#op-info").text("保存成功!");
        		}else{
	        		$("#op-info").text("保存失败!");
        		}
        	});
        }
    </script>

</body>
</html>