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
        
        	<div class="col-md-3">
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
					    <button type="button" id="deleteBtn" class="btn btn-default">删除</button>
					</div>
		        </div>
	        </div>
	        
	        <div class="col-md-9">
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
						<div class="panel panel-default console-panel">
							<div class="panel-heading">历史日志(最近10条)</div>
							<div class="panel-body">
								<table class="table table-striped">
							      <thead>
							        <tr>
							          <th>ID</th>
							          <th>状态</th>
							          <th>运行时间</th>
							          <th>结束时间</th>
							          <th>操作</th>
							        </tr>
							      </thead>
							      <tbody id="history-tbody">
							      </tbody>
							    </table>
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
    					url:"${path }/files/list.do",
    					autoParam:["id"]
    				},
    				view: {
    					selectedMulti: false
    				},
    				edit: {
    					enable: true,
    					showRemoveBtn: false,
    					showRenameBtn: false
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
        	
        	initContextMenuFunction();
        	$("#run-btn").click(runBtnClick);
        	$("#save-content-btn").click(saveContentBtnClick);
        	$("#editbox").keydown(function(){
        		$("#op-info").text("已修改未保存");
        	});

        }

        function refreshHistoryView(fileId) {
            $.post("${path }/files/historylist.do", { fileId: fileId}, function (data) {
                $("#history-tbody").html("");

                $.each(data, function (key, his) {

                    var td = "<tr><td>"+his.id+"</td><td>"+his.status+"</td><td>"+his.startTime+"</td><td>"+his.endTime+"</td><td>";
                    td+='<button type="button" class="btn btn-default btn-xs">查看日志</button>';
                    if(his.status=="running"){
                        td+=',<button type="button" class="btn btn-primary btn-xs">取消任务</button>';
                    }
                    td+="</td></tr>"
                    $("#history-tbody").append(td);
                });
            });
        }

        function resetToolBar(){
        	$("#save-content-btn").attr("disabled","disabled");
        	$("#run-btn").attr("disabled","disabled");
        	$("#run-select-btn").attr("disabled","disabled");
        	$("#editbox").attr("disabled","disabled");
        }
        
        function initContextMenuFunction(){
        	$("#addFolderBtn").bind("click", {isParent:true}, add);
        	$("#addFileBtn").bind("click", {isParent:false}, add);
        	$("#addHiveBtn").bind("click", {isParent:false}, add);
        	$("#addShellBtn").bind("click", {isParent:false}, add);
        	$("#deleteBtn").bind("click", {isParent:false}, add);
        }

        function hideRMenu() {
			if (rMenu) rMenu.css({"visibility": "hidden"});
			$("body").unbind("mousedown", onBodyMouseDown);
		}

        function add(e) {
        	hideRMenu();
        	
			var zTree = $.fn.zTree.getZTreeObj("tree"),
			isParent = e.data.isParent,
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			if (treeNode) {
				var name = "New File";
				var type = "File";
				if(isParent){
					name="New Folder";
					type="Folder";
				}
				
				$.post("${path }/files/add.do",{"name":name, isParent:isParent, "type":type,"parentId":treeNode.id},function(res){
					if(res.success){
						refreshNode("refresh", false);
					}else{
						alert("Error");
					}
				});
			}
		};
		
		function refreshNode(type,silent) {
			var zTree = $.fn.zTree.getZTreeObj("tree"),
			nodes = zTree.getSelectedNodes();
			if (nodes.length == 0) {
				alert("请先选择一个父节点");
			}
			for (var i=0, l=nodes.length; i<l; i++) {
				zTree.reAsyncChildNodes(nodes[i], type, silent);
				if (!silent) zTree.selectNode(nodes[i]);
			}
		}

		
        function OnLeftClick(event, treeId, treeNode) {
        	if(!treeNode.folder){
        		$.post("${path }/files/content.do",{fileId:treeNode.id},function(data){
        			$("#editbox").val(data);
        			$("#op-info").text("已打开: "+treeNode.name);
        			$("#editing-file-input").val(treeNode.id);
        			$("#editbox").removeAttr("disabled");
        			$("#save-content-btn").removeAttr("disabled");
                	$("#run-btn").removeAttr("disabled");
                	$("#run-select-btn").removeAttr("disabled");
                    refreshHistoryView($("#editing-file-input").val());
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
        	zTree.selectNode(treeNode);
        	showRMenu(treeNode.folder, event.clientX, event.clientY);
		}

		function showRMenu(isFolder, x, y) {
			$("#rMenu button").show();
			if (isFolder) {
				
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
            $("#run-btn").attr("disabled","disabled");
            $("#op-info").val("检测是否正在运行...");
            var isRunning;
            $.post("${path }/files/isrun.do",{fileId:$("#editing-file-input").val()},function(res){
                if(res){
                    alert("历史任务运行未结束,请稍后重试.")
                    isRunning=true;
                }
            });

            if(isRunning) return;

            $("#op-info").val("运行中...");
        	$.post("${path }/files/run.do",{content:$("#editbox").val(),fileId:$("#editing-file-input").val()},function(res){
        		if(res!=-1){
        			var timeId = setInterval(function(){
                	    $.post("${path }/files/gethistory.do",{historyId:res},function(res){
                	    	if(res.status=="END" || res.status=="FAILED" ){
                	    		clearTimeout(timeId);
                                $("#op-info").val("运行结束");
                                $("#run-btn").removeAttr("disabled");
                                refreshHistoryView($("#editing-file-input").val());
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
        	$.post("${path }/files/updatecontent.do",{content:$("#editbox").val(),fileId:$("#editing-file-input").val()},function(res){
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