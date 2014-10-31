$(document).ready(init);
var editor,scriptView,refreshJobLogTimer;
function init() {
    initLeftTree();
    initToolBar();
    initEditor();
    $("#right-content-div").hide();
    
    $("#click-refresh-link").click(function(){
    	 refreshHistoryView($("#viewing-job-input").val());
    });

    //搜索按钮
    $("#search-link").click(function () {
        if ($("#search-input").val().length < 2) {
            alert("请至少输入2个字符");
            return;
        }
        $.post(BASE_PATH + "/jobs/search.do", {key: $("#search-input").val()}, function (res) {
            $("#search-result-ul").html("");
            $.each(res, function (key, data) {
                var line = "<li class='list-group-item'><a href='javascript:loadJob(" + data.id + ");'>" + data.name + "</a></li>"
                $("#search-result-ul").append(line);
            });
            if (res.length == 0) {
                $("#search-result-ul").append("无结果");
            }
        });
    });
    
}

function initLeftTree() {
    $.fn.zTree.init($("#tree"), {
        async: {
            enable: true,
            url: BASE_PATH+"/jobs/list.do",
            autoParam: ["id"]
        },
        view: {
            selectedMulti: false
        },
        edit: {
            enable: true,
            showRemoveBtn: showRemoveBtn,
            showRenameBtn: showRenameBtn
        },
        callback: {
            onRightClick: OnRightClick,
            onClick: OnLeftClick,
            onRename: zTreeOnRename,
            onRemove: zTreeOnRemove,
            beforeRemove: zTreeBeforeRemove,
            onExpand: zTreeOnExpand
        }
    });

    zTree = $.fn.zTree.getZTreeObj("tree");
    rMenu = $("#rMenu");
    hideRMenu();
    initContextMenuFunction();
}

function showRenameBtn(treeId, treeNode){
	return treeNode.isParent;
}

function showRemoveBtn(treeId, treeNode){
	return treeNode.isParent;
}

function zTreeOnRename(event, treeId, treeNode, isCancel) {
	$.post(BASE_PATH+"/jobs/updategroupname.do", {id:treeNode.id,name:treeNode.name},function(res){
		if(res) alert("重命名成功.");
		else alert("重命名失败，请刷新页面重试.");
	});
}

function zTreeOnExpand(){

    $.post(BASE_PATH + "/jobs/scheduledjobs.do", function (res) {
        $.each(res, function (key, data) {
            var nodes = zTree.getNodesByParam("id", data);
            for (var i = 0, l = nodes.length; i < l; i++) {
                if (nodes[i].isParent == false) {
                    zTree.setting.view.fontCss = {};
                    zTree.setting.view.fontCss["color"] = "green";
                    zTree.updateNode(nodes[i]);
                    zTree.setting.view.fontCss = {};
                    break;
                }
            }
        });
    });


	$.post(BASE_PATH+"/jobs/failedjobs.do",function(res){
	   	 $.each(res, function (key, data) {
	   		 var nodes = zTree.getNodesByParam("id", data);
	   		 for (var i=0, l=nodes.length; i < l; i++) {
	 		 	if(nodes[i].isParent==false){
	 		 		zTree.setting.view.fontCss = {};
	 		 		zTree.setting.view.fontCss["color"] = "red";
	 		 		zTree.updateNode(nodes[i]);
	 		 		zTree.setting.view.fontCss = {};
	 		 		break;
	 		 	}
	 		 }
	   	 });
	});
}

function zTreeBeforeRemove(treeId, treeNode) {
	return confirm("确认删除？");
}

function zTreeOnRemove(event, treeId, treeNode) {
	$.post(BASE_PATH+"/jobs/deletegroup.do", {id:treeNode.id},function(res){
		if(res.success) alert("删除成功.");
		else {
			zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.reAsyncChildNodes(zTree.getNodes()[0], "refresh", false);
			alert(res.message);
		}
	});
}

function killJob(historyId){
	type=$("#job-type-td").text().trim();
	if(type=="hive"){
		  $.post(BASE_PATH+"/jobs/killhive.do",{historyId:historyId},function(res){
			  if(res==1){
				  alert("取消成功");
			  }else  if(res==2){
				  alert("取消失败");
			  }else  if(res==3){
				  alert("任务已经结束");
			  }else  if(res==4){
				  alert("请稍后再取消,未能找到任务ID");
			  }
		  });
	}else if(type=="shell"){
		 $.post(BASE_PATH+"/jobs/killshell.do",{historyId:historyId},function(res){
			 if(res){
				  alert("取消成功");
			  }else{
				  alert("取消失败");
			  }
		  });
	}
}
function refreshHistoryView(jobId) {
    $.post(BASE_PATH+"/jobs/history.do", { jobId: jobId}, function (data) {
        $("#history-tbody").html("");

        $.each(data, function (key, his) {

            var td = "<tr><td>" + his.id + "</td><td>" + his.status + "</td><td>" + his.startTime + "</td><td>" + his.endTime + "</td><td>";
            td += '<button type="button" class="btn btn-default btn-xs" onclick="viewLog('+his.id+')">查看日志</button>';
            if (his.status == "RUNNING" ) {
                td += ',<button type="button" class="btn btn-primary btn-xs" onclick="killJob('+his.id+')">取消任务</button>';
            }
            td += "</td></tr>"
            $("#history-tbody").append(td);
        });
    });
}

function viewLog(historyId){
	if(refreshJobLogTimer!=undefined){
		clearTimeout(refreshJobLogTimer);
		$("#log-his-p").html("");
		document.getElementById('log-div').scrollTop = 0;
	}
	
    $.post(BASE_PATH+"/jobs/gethistorylog.do",{historyId:historyId},function(res){
    	$("#log-his-p").html("");
    	$("#logModal").modal("show");
    	
    	if (res.status == "SUCCESS" || res.status == "FAILED") {
    		var cr = $("#log-his-p").html();
            var nr = res.log.replace(/\n/g, "<br>");
            $("#log-his-p").html(nr);
            document.getElementById('log-div').scrollTop = document.getElementById('log-div').scrollHeight;
    	}else{
    		 refreshJobLogTimer = setInterval(function () {
    	            $.post(BASE_PATH+"/jobs/gethistorylog.do", {historyId:historyId}, function (res) {
    	                var cr = $("#log-his-p").html();
    	                var nr = res.log.replace(/\n/g, "<br>");
    	                if (cr != nr) {
    	                    $("#log-his-p").html(nr);
    	                    document.getElementById('log-div').scrollTop = document.getElementById('log-div').scrollHeight;
    	                }
    	                if (res.status == "SUCCESS" || res.status == "FAILED") {
    	                    clearTimeout(refreshJobLogTimer);
    	                    refreshHistoryView($("#viewing-job-input").val());
    	                }
    	            });

    	        }, 1000);
    	}
    	
    });
}

function initToolBar() {
    $("#edit-btn").click(function () {
        $('#editModal').modal({
            backdrop: 'static',
            keyboard: false
        });
        //从隐藏层显示,需要延迟刷新,否则仍无法正常显示
        setTimeout(function(){
        	editor.refresh();
        },200);
        
        var dt = $.fn.zTree.getZTreeObj("dependencyTree");
        dt.reAsyncChildNodes(null , "refresh", true);
    });
    
    $("#file-upiload-input").uploadify({  
        'buttonText' : '请选择文件',  
        'multi'    : false,
        'fileSizeLimit' : '100MB',
        'fileTypeDesc' : 'ZIP文件',
        'fileTypeExts' : '*.zip',
        'height' : 30,  
        'swf' : BASE_PATH+'/uploadify/uploadify.swf',  
        'uploader' : BASE_PATH+'/upload/uploadFile.do',  
        'width' : 80,  
        'auto':false,  
        'fileObjName'   : 'file',
        'formData' : {
            'jobId' : ''
        },
        'onUploadStart' : function(file) {
            $("#file-upiload-input").uploadify("settings", "formData", {"jobId" : $("#viewing-job-input").val()});
        },
        'onUploadSuccess' : function(file, data, response) {  
            alert( file.name + ' 上传成功！ ');
            $.post(BASE_PATH+"/upload/exist.do",{jobId:$("#viewing-job-input").val()},function(res){
       		 $("#uploaded-rs-label").html(res);
       	 	});
        }
    });
    
    $("#do-upload-btn").click(function(){
        $("#file-upiload-input").uploadify('upload');
    });
    
    $.fn.zTree.init($("#dependencyTree"), {
    	check: {
    		enable: true,
    		chkboxType: {"Y": "", "N": ""}
    	},
    	async: {
    		enable: true,
    		url: BASE_PATH+"/jobs/list.do",
    		autoParam: ["id"]
    	},
    	view: {
    		dblClickExpand: false
    	},
    	callback: {
    		beforeClick: beforeClick,
    		onCheck: onCheck
    	}
    });

    $("#manual-run-btn").click(function () {
    	var org = $("#manual-run-btn").html();
    	$("#manual-run-btn").attr("disabled","disabled");
    	setTimeout(function(){
    		refreshHistoryView($("#viewing-job-input").val());
    	},2000)
    	$.post(BASE_PATH+"/jobs/manualrun.do",{jobId:$("#viewing-job-input").val()},function(res){
    		alert("已进入任务队列");
    		$("#manual-run-btn").removeAttr("disabled");
    		$("#manual-run-btn").html(org);
    		refreshHistoryView($("#viewing-job-input").val());
    	});
    });
    
    $("#resume-run-btn").click(function () {
    	var org =$("#resume-run-btn").html();
    	$("#resume-run-btn").attr("disabled","disabled");
    	$("#resume-run-btn").html("运行中...");
    	setTimeout(function(){
    		refreshHistoryView($("#viewing-job-input").val());
    	},2000)
    	$.post(BASE_PATH+"/jobs/resumerun.do",{jobId:$("#viewing-job-input").val()},function(res){
    		$("#resume-run-btn").removeAttr("disabled");
    		$("#resume-run-btn").html(org);
    		if(res){
    			alert("已加入运行队列");
    		}else{
    			alert("ERROR:运行失败");
    		}
    	});
    });
    $("#open-close-btn").click(function () {
    	var org = $("#open-close-btn").html();
    	$("#open-close-btn").attr("disabled","disabled");
    	$("#open-close-btn").html("处理中...");
    	$.post(BASE_PATH+"/jobs/openclosejob.do",{id:$("#viewing-job-input").val()},function(res){
    		$("#open-close-btn").removeAttr("disabled","disabled");
    		$("#open-close-btn").html(org);
    		if("opened"==res) {
    			$("#auto-td").html("开启");
    			alert("开启成功");
    		}
    		if("closed"==res) {
    			$("#auto-td").html("关闭");
    			alert("关闭成功");
    		}
    	});
    });
    $("#delete-btn").click(function () {
    	 $.post(BASE_PATH+"/jobs/deletejob.do", { id: $("#viewing-job-input").val() },function(res){
    		 var treeObj = $.fn.zTree.getZTreeObj("tree");
    		 var nodes = treeObj.getSelectedNodes();
    		 for (var i=0, l=nodes.length; i < l; i++) {
    		 	treeObj.removeNode(nodes[i]);
    		 }
    		 if(res.success){
    			$("#right-content-div").hide();
    			alert("删除成功"); 
    		 }else{
    			 alsert(res.message);
    		 }
    	 });
    });
    
    $("#upload-resource-btn").click(function(){
    	 $('#uploadModal').modal({
             backdrop: 'static',
             keyboard: false
         });
    	 
    	 $.post(BASE_PATH+"/upload/exist.do",{jobId:$("#viewing-job-input").val()},function(res){
    		 $("#uploaded-rs-label").html(res);
    	 });
    });
    
    $("#update-job-btn").click(function () {
        $.post(BASE_PATH+"/jobs/update.do", {
                    id: $("#viewing-job-input").val(),
                    name: $("#inputName").val(),
                    runType: $("#inputScheduleType").val(),
                    scheduleType: $('input[type="radio"][name="scheduleType"]:checked').val(),
                    cron: $("#inputCron").val(),
                    dependencies: $("#dependenciesSel").val(),
                    script: editor.getValue()
                },
                function (res) {
                    if (res == false) {
                        alert("操作失败!");
                        return;
                    }

                    var zTree = $.fn.zTree.getZTreeObj("tree"),
                            nodes = zTree.getSelectedNodes();
                    nodes[0].name = $("#inputName").val() + "[" + $("#viewing-job-input").val() + "]";
                    zTree.updateNode(nodes[0]);
                    
                    $('#editModal').modal('hide');
                    freshJobView($("#viewing-job-input").val());
                    alert("修改成功!");
                });
    });
}


function initEditor(){
	editor = CodeMirror.fromTextArea(document.getElementById("edit-script"), {
		lineNumbers : true,
		mode : 'text/x-hive',
		indentWithTabs : true,
		smartIndent : true,
		matchBrackets : true,
		autofocus : true,
		width: '100%',
        height: '400px'
	});
	
	scriptView = CodeMirror.fromTextArea(document.getElementById("script-p"), {
		lineNumbers : true,
		mode : 'text/x-hive',
		indentWithTabs : true,
		smartIndent : true,
		matchBrackets : true,
		autofocus : true,
		readOnly : true,
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
    for (var i = 0, l = nodes.length; i < l; i++) {
        v += nodes[i].id + ",";
    }
    if (v.length > 0) v = v.substring(0, v.length - 1);
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
    if (!(event.target.id == "dependenciesSel" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
        hideMenu();
    }
}

function initContextMenuFunction() {
    $("#add-group-btn").bind("click", {isParent: true}, add);
    $("#add-job-btn").bind("click", {isParent: false}, add);
    $("#delete-btn").bind("click", deleteJob);
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
        var name = "新文件";
        var type = "File";
        if (isParent) {
            name = "新目录";
            type = "Folder";
	        $.post(BASE_PATH+"/jobs/addgroup.do", {"name": name, parentId: treeNode.id}, function (res) {
	            if (res.success) {
	                refreshNode("refresh", true);
	            } else {
	                alert("Error");
	            }
	        });
        }else{
        	$.post(BASE_PATH+"/jobs/addjob.do", {"name": name, isParent: isParent, "type": type, "groupId": treeNode.id}, function (res) {
	            if (res.success) {
	                refreshNode("refresh", false);
	            } else {
	                alert("Error");
	            }
	        });
        }

    }
}
;

function refreshNode(type, silent) {
    var zTree = $.fn.zTree.getZTreeObj("tree"),
            nodes = zTree.getSelectedNodes();
    if (nodes.length == 0) {
        alert("请先选择一个父节点");
    }
    for (var i = 0, l = nodes.length; i < l; i++) {
        zTree.reAsyncChildNodes(nodes[i], type, silent);
        if (!silent) zTree.selectNode(nodes[i]);
    }
    
    //编辑页面的依赖树
//   var dt = $.fn.zTree.getZTreeObj("dependencyTree");
//   dt.reAsyncChildNodes(null , "refresh", true);
}

function deleteJob(){
	
}

function OnLeftClick(event, treeId, treeNode) {
    if (!treeNode.folder) {
        loadJob(treeNode.id)
    }
}

function loadJob(jobId) {
    freshJobView(jobId);
    refreshHistoryView(jobId);
    $("#right-content-div").removeClass("hidden");
    $("#right-content-div").show();
}
function freshJobView(jobId) {
    $.post(BASE_PATH+"/jobs/get.do", { id: jobId}, function (data) {

        $("#job-id-td").text(data.id);
        $("#job-type-td").text(data.runType);
        $("#name-td").text(data.name);
        $("#run-type-td").text(data.scheduleType == 1 ? "定时调度" : "依赖调度");
        $("#auto-td").text(data.auto == 0 ? "关闭" : "开启");
        $("#run-time-td").text(data.scheduleType == 1 ? data.cron : data.dependencies);
//        if(data.script) $("#script-p").html(data.script.replace(/\n/gi, "<br/>").replace(/\r/gi, "<br/>"));
        scriptView.setValue(data.script==null?"":data.script);
        $("#viewing-job-input").val(data.id);

        $("#inputName").val(data.name);
        $("#inputScheduleType").val(data.runType);

        if (data.scheduleType == 1) {
            $("#radioSchedualByTime").prop("checked",true);
            $("#radioSchedualByDependency").prop("checked",false);
            $("#inputCron").val(data.cron);
        } else {
        	$("#radioSchedualByTime").prop("checked",false);
            $("#radioSchedualByDependency").prop("checked",true);
            $("#dependenciesSel").val(data.dependencies);
        }
        editor.setValue(data.script==null?"":data.script);
    });
}


function OnRightClick(event, treeId, treeNode) {
    if (treeNode == null) return;
    zTree.selectNode(treeNode);
    showRMenu(treeNode.isRoot, treeNode.folder, event.clientX, event.clientY);
}

function showRMenu(isRoot, isFolder, x, y) {
    $("#rMenu button").show();

    if (isFolder) {
        $("#add-group-btn").hide();
    } else {
        $("#add-group-btn").hide();
        $("#add-job-btn").hide();
    }

    if (isRoot) {
        $("#add-job-btn").hide();
        $("#add-group-btn").show();
    }
    
    rMenu.css({"top": y + "px", "left": x + "px", "visibility": "visible"});

    $("body").bind("mousedown", onBodyMouseDown);
}
function hideRMenu() {
    if (rMenu) rMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", onBodyMouseDown);
}
function onBodyMouseDown(event) {
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length > 0)) {
        rMenu.css({"visibility": "hidden"});
    }
}