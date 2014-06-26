<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%    
String path = request.getContextPath();    
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
pageContext.setAttribute("path",path);    
pageContext.setAttribute("basePath",basePath);    
%>


<%
    if(request.getAttribute("files")==null){
        request.getRequestDispatcher("/hdfs_browse/list.do").forward(request,response);
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

<title>Pangu hdfs browse</title>

<link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">

<link href="<%=path %>/css/hdfs_browse.css" rel="stylesheet">

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
            <div class="col-md-6">
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                       	 操作 <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">移动</a></li>
                        <li><a href="#">删除</a></li>
                    </ul>
                </div>
                <button type="button" class="btn btn-default">返回上级</button>
            </div>
            <div class="col-md-6 text-right">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <label class="sr-only col-md-4" for="hdfsPathInput">HDFS PATH</label>
                        <input type="text" class="form-control" id="hdfsPathInput" placeholder="HDFS PATH">
                    </div>
                    <button type="submit" class="btn btn-default">GO</button>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="div-result">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th><input type="checkbox" class="check-all"></th>
                            <th>文件名</th>
                            <th>类型</th>
                            <th>大小</th>
                            <th>修改时间</th>
                            <th>拥有者</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody class="table-tbody">
                        <c:forEach items="${files}" var="file">
                            <tr>
                                <td><input type="checkbox"></td>
                                <td><a href="#" class="item-name-href">${file.name}</a></td>
                                <td>${file.type}</td>
                                <td>${file.size}</td>
                                <td>${file.modificationTime}</td>
                                <td>${file.owner}</td>
                                <td>
                                    <button type="button" class="btn-delete-item btn btn-default btn-xs" data="${file.name}">删除</button>
                                    <button type="button" class="btn-move-item btn btn-default btn-xs" data="${file.name}">移动</button>
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

	<!-- 移动文件/文件夹  Modal -->
	<div class="modal fade" id="moveFileModal" tabindex="-1" role="dialog" aria-labelledby="moveFileModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-body">
	        <form class="form-horizontal" role="form">
			  <div class="form-group">
			    <label for="inputEmail3" class="col-sm-2 control-label">原路径</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="srcPathInput" disabled>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="inputPassword3" class="col-sm-2 control-label">新路径</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="dstPathInput">
			    </div>
			  </div>
			  <div class="form-group text-right">
			    <div class="col-sm-offset-2 col-sm-10">
			    	<button type="submit" class="btn btn-default">取消</button>
			      	<button type="submit" class="btn btn-primary">移动</button>
			    </div>
			  </div>
			</form>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${path }/js/jquery-1.11.1.min.js"></script>
	<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(document).ready(init);
        function init(){
            $(".check-all").click(function(){
                var flag = this.checked;
                $(".table-tbody input[type='checkbox']").each(function () {//查找每一个Id以Item结尾的checkbox
                    $(this).prop("checked",flag);
                });

            });

            $(".table-tbody input[type='checkbox']").click(function(){
                if ($(".table-tbody input[type='checkbox']:checked").length == $(".table-tbody input[type='checkbox']").length) {
                    $(".check-all").prop("checked","checked");
                }
                else $(".check-all").removeAttr("checked");
            });


            $(".item-name-href").click(function(target){
                var org = $("#current_path").val();

                $("#nextPath").val(org+"/"+this.innerText);

                $("#enterFolderForm").submit();

            });
            
            $(".btn-delete-item").click(function(){
            	var org = $("#current_path").val();
            	
            	if(window.confirm("确认删除吗?")){
	                
            		$.post("/hdfs_browse/delete.do",{path:org+"/"+$(this).attr("data")},function(res){
            			if(res.success){
            				self.location="/hdfs_browse/list.do?path="+org;
            			}else{
            				alert("删除失败,"+res.errorMessage);
            			}
            		});
            	}
            });
            
            $(".btn-move-item").click(function(){
            	var org = $("#current_path").val();
            	$("#srcPathInput").val(org+"/"+$(this).attr("data"));
            	$("#moveFileModal").modal({backdrop:'static'},"show");
            });
        }
    </script>

    <form id="enterFolderForm" action="${path }/hdfs_browse/list.do" method="post">
        <input type="hidden" id="nextPath" name="path" value="">
    </form>

    <input type="hidden" id="current_path" value="${current_path}">
</body>
</html>