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

    <title>---</title>

    <link href="${path }/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${path }/css/cloud_test.css" rel="stylesheet">
    <link href="${path }/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	<link href="${path }/codemirror/lib/codemirror.css" rel="stylesheet" >
	<link href="${path }/uploadify/uploadify.css" rel="stylesheet" >
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

            <div class="row">
                <div class="col-md-12">
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
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="input-group">
                        <input class="form-control" type="text" id="search-input" placeholder="输入关键词">
                        <span class="input-group-btn">
                            <button class="btn btn-default" id="search-link" type="button">搜!</button>
                        </span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <ul class="list-group" id="search-result-ul">
                    </ul>
                </div>
            </div>

        </div>

        <div class="col-md-9 hidden" id="right-content-div">
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

                                 <button type="button" class="btn btn-default" id="upload-resource-btn">
                                    <span class=" "></span>资源管理
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
                                                <td><strong>ID:</strong></td>
                                                <td id="job-id-td">?</td>
                                                <td><strong>任务类型:</strong></td>
                                                <td id="job-type-td">?</td>
                                            </tr>
                                            <tr>
                                                <td><strong>名称:</strong></td>
                                                <td id="name-td">?</td>
                                                <td><strong>调度类型:</strong></td>
                                                <td id="run-type-td">?</td>
                                            </tr>
                                            <tr>
                                                <td><strong>调度状态:</strong></td>
                                                <td id="auto-td">?</td>
                                                <td><strong>依赖/定时:</strong></td>
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
                            	<textarea id="script-p">?</textarea>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default console-panel">
                        <div class="panel-heading">历史日志(最近10条),<a href="javascript:void(0);" id="click-refresh-link">点击刷新</a></div>
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
                        <label for="" class="col-sm-2 control-label">依赖/定时</label>

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
                                <input type="text" class="form-control" id="dependenciesSel" name="dependencies"
                                       readonly="readonly" onclick="showMenu();">

                                <div id="menuContent" style="display:none;" class="zTreeDemoBackground left">
                                    <ul id="dependencyTree" class="ztree">
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="col-sm-2 control-label">脚本</label>

                        <div class="col-sm-10">
                        </div>
                    </div>

                    <div class="form-group">
						<textarea id="edit-script"></textarea>
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

<div class="modal fade in" id="logModal" tabindex="-1" role="dialog"
     aria-labelledby="logModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="logModalLabel">运行日志</h4>
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



<div class="modal fade in" id="uploadModal" tabindex="-1" role="dialog"
     aria-labelledby="uploadModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="">资源上传</h4>
            </div>
            <div class="modal-body">
                <form role="form">
					  <div class="form-group">
                          <label for="">资源文件</label>
                          <input type="file" id="file-upiload-input">
					    <p class="help-block">请上传zip格式文件,上传后会自动解压,每次上传都会删除老资源,文件最大为100M.</p>
					  </div>
					  <button type="button" class="btn btn-primary" id="do-upload-btn">上传</button>
				</form>
				
				<div>
					已上传资源:<label id="uploaded-rs-label"></label>
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
<input type="hidden" id="viewing-job-input" value=""/>
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="${path }/js/jquery-1.11.1.min.js"></script>
<script src="${path }/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	var BASE_PATH='${path }';
</script>
<script type="text/javascript" src="${path }/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${path }/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${path }/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="${path }/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="${path }/codemirror/mode/sql/sql.js"></script>
<script type="text/javascript" src="${path }/uploadify/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${path }/js/cloud_job.js"></script>

</body>
</html>