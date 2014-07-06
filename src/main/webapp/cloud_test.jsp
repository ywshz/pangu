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
<link href="<%=path %>/jstree/themes/default/style.min.css"rel="stylesheet" />

<style type="text/css">

#tree { float:left; min-width:319px; border-right:1px solid silver; overflow:auto; padding:0px 0; }

	#tree .folder { background:url('./img/file_sprite.png') right bottom no-repeat; }
	#tree .file { background:url('./img/file_sprite.png') 0 0 no-repeat; }

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
	        			<div id="tree">
			        		<ul>
							    <li>个人文档
							      <ul>
							        <li id="child_node1" type="file">1.sh</li>
							        <li id="child_nod2e" rel="file">1.sh</li>
							        <li id="child_node3">1.sh</li>
							        <li id="child_node4">1.sh</li>
							      </ul>
							    </li>
							 </ul>
	        			</div>
		        	</div>
		        </div>
	        </div>
	        
	        <div class="col-md-10">
	        	<div class="panel panel-default">
					<div class="panel-body">
					
						<div>
							<div id="run-toolbar-div">
							
								<button type="button" class="btn btn-default" id="public-var-btn">
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

	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
    <script src="/jstree/jstree.js"></script>
    <script type="text/javascript">
        $(document).ready(init);
        function init(){
        	$('#tree').jstree({
        		"core":{
        			data:{
        				url:'/files/list.do',
        				data:function(node){
        					return {"id":node.id};
        				}
        			}
        		},
        		"contextmenu":{
        			"items":customMenu
        		},
        		'types' : {
					'default' : { 'icon' : 'glyphicon glyphicon-folder-open' },
					'file' : { 'icon' : 'glyphicon glyphicon-file' }
				},
        		"plugins" : [
        		             "contextmenu", "dnd",  "types"
        		           ]
        	});
        	
        	
        	$("#run-btn").click(runBtnClick);
        }
        
        function customMenu(node) {
            // The default set of all items
            var items = {
    				newFolder:{
    					label:"新文件夹",
    					action:function(){alert(1);}
    				},
    				newFile:{
    					label:"新文件",
    					action:function(){alert(1);}
    				},
    				deleteFile:{
    					label:"删除",
    					action:function(){alert(1);}
    				}
    		};

            if (node.type=="file") {
            	items.newFolder._disabled = true;
            	items.newFile._disabled = true;
            }else{
            	items.newFolder._disabled = false;
            	items.newFile._disabled = false;
            }
            return items;
        }
        
        function runBtnClick(){
        	$.post("/files/run.do",{content:$("#editbox").val()});
        	
        	var timeId = setInterval(function(){
        	    $.post("/files/getlog.do",{jobId:1},function(res){
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
        	
        }
    </script>

</body>
</html>