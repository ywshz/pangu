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
						<button type="button" id="add-group-btn" class="btn btn-default">创建组</button>
					    <button type="button" id="add-job-btn" class="btn btn-default">添加任务</button>
					</div>
		        </div>
	        </div>
	        
	        <div class="col-md-9">
	        	<div class="panel panel-default">
					<div class="panel-body">
					
						<div>
							<div id="run-toolbar-div" class="row">
								<div class="col-md-12">
									<button type="button" class="btn btn-primary" id="edit-btn">
										 <span class=" "></span>编辑
									</button>
									
									<button type="button" class="btn btn-default" id="manual-run-btn">
										<span class=" "></span>手动执行
									</button>
									
									<button type="button" class="btn btn-default" id="resume-run-btn">
										<span class=" "></span>手动恢复
									</button>
									
									<button type="button" class="btn btn-default" id="open-close-btn">
										<span class=" "></span>开启/关闭
									</button>
									
									<button type="button" class="btn btn-default" id="delete-btn">
										<span class=" "></span>删除
									</button>
								</div>
							</div>
							
							
							<div class="row">
								<div class="col-md-12">
									<div class="panel panel-default">
									<div class="panel-heading">基本信息</div>
										<div class="panel-body">
											<table class="table">
											<tr>
												<td> <strong>ID:</strong></td>
												<td id="job-id-td">?</td>
												<td> <strong>任务类型:</strong></td>
												<td id="job-type-td">?</td>
											</tr>
											<tr>
												<td> <strong>名称:</strong></td>
												<td id="name-td">?</td>
												<td> <strong>调度类型:</strong></td>
												<td id="run-type-td">?</td>
											</tr>
											<tr>
												<td> <strong>调度状态:</strong></td>
												<td id="auto-td">?</td>
												<td> <strong>依赖/定时:</strong></td>
												<td id="run-time-td">?</td>
											</tr>
											</table>
										</div>
									</div>
								</div>
							</div>
							
							<div class="panel panel-default">
								<div class="panel-heading">脚本</div>
								<div class="panel-body">
									<div id="console-div" style='height:200px;overflow: auto;'>
										<p id="script-p">?</p>
									</div>
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
							        <tr>
							          <td>123</td>
							          <td>运行中</td>
							          <td>2014/12/12 12:12:12</td>
							          <td></td>
							          <td><button type="button" class="btn btn-default btn-xs">查看日志</button>,<button type="button" class="btn btn-primary btn-xs">取消任务</button></td>
							        </tr>
							        <tr>
							          <td>1</td>
							          <td>成功</td>
							          <td>2014/12/12 12:12:12</td>
							          <td>2014/12/12 12:14:12</td>
							          <td><button type="button" class="btn btn-default btn-xs">查看日志</button></td>
							        </tr>
							        <tr>
							        	<th colspan="5" class=" text-right">
							        		<div class="btn-group">
											  <button type="button" class="btn btn-default">上一页</button>
											  <button type="button" class="btn btn-default">下一页</button>
											</div>
							        	</th>
							        </tr>
							      </tbody>
							    </table>
							</div>
						</div>
					</div>
				</div>
	        </div>
	        
        </div>

	</div>


	<div class="modal fade  bs-example-modal-lg" id="editModal" tabindex="-1" role="dialog"
		aria-labelledby="editModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="editModalLabel">编辑</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="inputName" class="col-sm-2 control-label">名称</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="inputName" value="">
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">调度类型</label>
							<div class="col-sm-10">
								<select class="form-control" id="inputScheduleType">
								  <option value="hive">Hive脚本</option>
								  <option value="shell">Shell脚本</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="scheduleInput" class="col-sm-2 control-label">依赖/定时</label>
							<div class="col-sm-10">
								<div class="radio">
								  <label>
								    <input type="radio" name="scheduleType" id="radioSchedualByTime" value="1">
								            定时表达式
								  </label>
								  <input type="text" class="form-control" id="inputCron" value="0 0 0 * * ?">
								</div>
								<div class="radio">
								  <label>
								    <input type="radio" name="scheduleType" id="radioSchedualByDependency" value="2">
								    依赖
								  </label>
								  <input type="text"  class="form-control" id="dependenciesSel" name="dependencies" readonly="readonly" onclick="showMenu();"> 
								  
								  <div id="menuContent" style="display:none;" class="zTreeDemoBackground left">
					     			<ul id="dependencyTree" class="ztree">
									</ul>
			        			  </div>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label for="inputPassword3" class="col-sm-2 control-label">脚本</label>
							<div class="col-sm-10">
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-12">
								<textarea class="form-control" id="edit-script" name="script" rows="10" draggable="false" ></textarea>
							</div>
						</div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" id="update-job-btn" class="btn btn-primary">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- /.container -->
	
	<input type="hidden" id="editing-file-input" value=""/>
	<input type="hidden" id="viewing-job-input" value=""/>
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
    
	<script type="text/javascript" src="${path }/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.ztree.excheck-3.5.js"></script>
    
    <script type="text/javascript">
	
        $(document).ready(init);
        
        function init(){
        	initLeftTree();
        	initToolBar();
        }
        
        function initLeftTree(){
        	$.fn.zTree.init($("#tree"), {
    			async: {
    				enable: true,
    				url:"/jobs/list.do",
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
        	initContextMenuFunction();
        }
        
        function initToolBar(){
        	$("#edit-btn").click(function(){
        		$('#editModal').modal({
        			  backdrop: 'static',
        			  keyboard: false
        		});
        		
          		$.fn.zTree.init($("#dependencyTree"), {
          			check: {
          				enable: true,
          				chkboxType: {"Y":"", "N":""}
          			},
        			async: {
        				enable: true,
        				url:"/jobs/list.do",
        				autoParam:["id"]
        			},
          			view: {
          				dblClickExpand: false
          			},
          			callback: {
          				beforeClick: beforeClick,
        				onCheck: onCheck
          			}
          		});

        	});
        	
        	$("#manual-run-btn").click(function(){
        		
        	});
        	$("#resume-run-btn").click(function(){
        		
        	});
        	$("#open-close-btn").click(function(){
        		
        	});
        	$("#delete-btn").click(function(){
        		
        	});
			$("#update-job-btn").click(function(){
        		$.post("/jobs/update.do",{
        			id:$("#viewing-job-input").val(),
        			name:$("#inputName").val(),
        			runType:$("#inputScheduleType").val(),
        			scheduleType:$('input[type="radio"][name="scheduleType"]:checked').val(),
        			cron:$("#inputCron").val(),
        			dependencies:$("#dependenciesSel").val(),
        			script:$("#edit-script").val()
        			},
        			function(res){
        				if(res==false){
        					alert("操作失败!");
        					return;
        				}
        				
        				var zTree = $.fn.zTree.getZTreeObj("tree"),
        				nodes = zTree.getSelectedNodes();
        				nodes[0].name = $("#inputName").val()+"["+$("#viewing-job-input").val()+"]";
        				zTree.updateNode(nodes[0]);
        				$('#editModal').modal('hide');
        				freshJobView($("#viewing-job-input").val());
        				alert("修改成功!");
        		});
        	});
        }
        
        function beforeClick(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("dependencyTree");
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}
		
		function onCheck(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("dependencyTree"),
			nodes = zTree.getCheckedNodes(true),
			v = "";
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			$("#dependenciesSel").val(v);
		}

		function showMenu() {
			$("#menuContent").show();
			$("body").bind("mousedown", onBodyDown);
		}
		
		function hideMenu() {
			$("#menuContent").hide();
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "dependenciesSel" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
		
        function initContextMenuFunction(){
        	$("#add-group-btn").bind("click", {isParent:true}, add);
        	$("#add-job-btn").bind("click", {isParent:true}, add);
        	$("#delete-btn").bind("click", {isParent:false}, add);
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
				
				$.post("/files/add.do",{"name":name, isParent:isParent, "type":type,"parentId":treeNode.id},function(res){
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
        		freshJobView(treeNode.id);
        		refreshHistoryView(treeNode.id);
        	}
        }
        
        function freshJobView(jobId){
        	$.post("/jobs/get.do",{ id : jobId},function(data){
				
				$("#job-id-td").text(data.id);
				$("#job-type-td").text(data.runType);
				$("#name-td").text(data.name);
				$("#run-type-td").text(data.scheduleType==1?"定时调度":"依赖调度");
				$("#auto-td").text(data.auto==0?"关闭":"开启");
				$("#run-time-td").text(data.scheduleType==1 ? data.cron : data.dependencies);
				$("#script-p").html(data.script.replace(/\n/gi, "<br/>").replace(/\r/gi, "<br/>"));
				
				$("#viewing-job-input").val(data.id);
				
				$("#inputName").val(data.name);
				$("#inputScheduleType").val(data.runType);
				
				if(data.scheduleType==1){
					$("#radioSchedualByTime").attr("checked", "checked");
					$("#inputCron").val(data.cron);
				}else{
					$("#radioSchedualByDependency").attr("checked", "checked");
					$("#dependenciesSel").val(data.dependencies);
				}
				$("#edit-script").val(data.script);
    		});
        }
        
        function refreshHistoryView(jobId){
        	$.post("/jobs/history.do",{ jobId : jobId},function(data){
        		$("#history-tbody").html("");
        		
        		$.each(data, function(key, his) { 
        			alert(key + ':' + his.startTime); 
        		}); 
        	});
        }
        
        function OnRightClick(event, treeId, treeNode) {
        	if(treeNode==null) return;
        	zTree.selectNode(treeNode);
        	showRMenu(treeNode.isRoot, treeNode.folder, event.clientX, event.clientY);
		}

		function showRMenu(isRoot, isFolder, x, y) {
			$("#rMenu button").show();
			
			if(isRoot){
			    $("#add-job-btn").hide();	
			}
			
			if (isFolder) {
				$("#add-group-btn").hide();
			}else{
			    $("#add-group-btn").hide();
			    $("#add-job-btn").hide();
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
    </script>

</body>
</html>