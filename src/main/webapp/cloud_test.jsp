<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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

    <title>盘古 -- 云测试</title>

    <link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${path }/css/cloud_test.css" rel="stylesheet">
    <link href="${path }/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	<link href="${path }/codemirror/lib/codemirror.css" rel="stylesheet" >

    <style type="text/css">
        div#rMenu {
            position: fixed;
            visibility: hidden;
            top: 0;
            z-index: 1000
        }
    </style>
    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]>
    <script src="${path }/bootstrap/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<%@ include file="common/headmenu.jsp" %>

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
                    <button type="button" id="addHiveBtn" class="btn btn-default">创建Hive</button>
                </div>
            </div>
        </div>

        <div class="col-md-9 hidden" id="right-content-div">
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
                        <div class="panel-heading">历史日志(最近20条)<a href="javascript:void(0);" id="click-refresh-link">点击刷新</a></div>
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


<div class="modal fade in" id="logModal" tabindex="-1" role="dialog"
     aria-labelledby="logModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="editModalLabel">运行日志</h4>
            </div>
            <div class="modal-body">
                <div id="log-div" style='height:400px;overflow: auto;'>
                    <p id="log-his-p"></p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
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
<script type="text/javascript">
	var BASE_PATH='${path }';
</script>
<script type="text/javascript" src="${path }/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${path }/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="${path }/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="${path }/codemirror/mode/sql/sql.js"></script>
<script type="text/javascript" src="${path }/js/cloud_test.js"></script>

</body>
</html>