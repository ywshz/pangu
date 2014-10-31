$(document).ready(init);
var editor;

function init() {
	initLeftNavTree();
	
	initEditor();

	initToolbar();
	
	$("#right-content-div").hide();
	
	 $("#click-refresh-link").click(function(){
    	 refreshHistoryView($("#editing-file-input").val());
    });
}

function initLeftNavTree(){
	$.fn.zTree.init($("#tree"), {
		async : {
			enable : true,
			url : BASE_PATH + "/files/list.do",
			autoParam : [ "id" ]
		},
		view : {
			selectedMulti : false
		},
		edit : {
			enable : true,
			showRemoveBtn : true,
			showRenameBtn : true
		},
		callback : {
			onRightClick : OnRightClick,
			onClick : OnLeftClick,
			onRename : zTreeOnRename,
			onRemove : zTreeOnRemove,
			beforeRemove : zTreeBeforeRemove
		}
	});

	zTree = $.fn.zTree.getZTreeObj("tree");
	rMenu = $("#rMenu");
	hideRMenu();
	
	initContextMenuFunction();
}

function initEditor(){
	editor = CodeMirror.fromTextArea(document.getElementById("editbox"), {
		lineNumbers : true,
		mode : 'text/x-hive',
		indentWithTabs : true,
		smartIndent : true,
		matchBrackets : true,
		autofocus : true
	});
	editor.on('change',function(){
		$("#op-info").text("已修改未保存");
	});
}

function initToolbar(){
	 resetToolBar();
	 $("#run-btn").click(runBtnClick);
	 $("#save-content-btn").click(saveContentBtnClick);
}


function resetToolBar() {
    $("#save-content-btn").attr("disabled", "disabled");
    $("#run-btn").attr("disabled", "disabled");
    editor.setOption("readOnly",true);
}

function initContextMenuFunction() {
    $("#addFolderBtn").bind("click", {isParent: true}, add);
    $("#addHiveBtn").bind("click", {isParent: false}, add);
}

function refreshHistoryView(fileId) {
    $.post(BASE_PATH+"/files/historylist.do", { fileId: fileId}, function (data) {
        $("#history-tbody").html("");

        $.each(data, function (key, his) {

            var td = "<tr><td>" + his.id + "</td><td>" + his.status + "</td><td>" + his.startTime + "</td><td>" + his.endTime + "</td><td>";
            td += '<button type="button" class="btn btn-default btn-xs" onclick="viewLog('+his.id+')">查看日志</button>';
            if (his.status == "running") {
                td += ',<button type="button" class="btn btn-primary btn-xs">取消任务</button>';
            }
            td += "</td></tr>"
            $("#history-tbody").append(td);
        });
    });
}

function viewLog(historyId){
    $.post(BASE_PATH+"/files/gethistorylog.do",{id:historyId},function(res){
        var nr = res.replace(/\n/g, "<br>");

        $("#log-his-p").html(nr);

        $("#logModal").modal("show");
    });
}

function zTreeOnRename(event, treeId, treeNode, isCancel) {
	$.post(BASE_PATH+"/files/updatename.do", {id:treeNode.id,name:treeNode.name},function(res){
		if(res) alert("重命名成功.");
		else alert("重命名失败，请刷新页面重试.");
	});
}

function zTreeBeforeRemove(treeId, treeNode) {
	return confirm("确认删除？");
}

function zTreeOnRemove(event, treeId, treeNode) {
	$.post(BASE_PATH+"/files/delete.do", {id:treeNode.id},function(res){
		if(res) alert("删除成功.");
		else{
			zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.reAsyncChildNodes(zTree.getNodes()[0], "refresh", false);
			alert("不能删除根目录,或者仍有子节点的目录");
		}
	});
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
        if (isParent) {
            name = "New Folder";
            type = "Folder";
        }

        $.post(BASE_PATH+"/files/add.do", {"name": name, isParent: isParent, "type": type, "parentId": treeNode.id}, function (res) {
            if (res.success) {
                refreshNode("refresh", false);
            } else {
                alert("Error");
            }
        });
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
}


function OnLeftClick(event, treeId, treeNode) {
    if (!treeNode.folder) {
        $.post(BASE_PATH+"/files/content.do", {fileId: treeNode.id}, function (data) {
        	editor.setOption("readOnly",false);
            editor.setValue(data);
            $("#op-info").text("已打开: " + treeNode.name);
            $("#editing-file-input").val(treeNode.id);
            $("#save-content-btn").removeAttr("disabled");
            $("#run-btn").removeAttr("disabled");
            refreshHistoryView($("#editing-file-input").val());
        });
        $("#right-content-div").removeClass("hidden");
        $("#right-content-div").show();
    } else {
        resetToolBar();
        $("#op-info").text("");
        editor.setValue("")
        editor.setOption("readOnly",true);
        $("#editing-file-input").val("");
        $("#history-tbody").html("");
    }
}

function OnRightClick(event, treeId, treeNode) {
    if (treeNode == null) return;
    zTree.selectNode(treeNode);
    showRMenu(treeNode.folder, event.clientX, event.clientY);
}

function showRMenu(isFolder, x, y) {
    $("#rMenu button").show();
    if (isFolder) {

    } else {
        $("#addFolderBtn").hide();
        $("#addFileBtn").hide();
        $("#addHiveBtn").hide();
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

function runBtnClick() {
    $("#log-p").html("");
    $("#run-btn").attr("disabled", "disabled");
    $("#op-info").val("运行中...");
    $.post(BASE_PATH+"/files/run.do", {content: editor.getValue(), fileId: $("#editing-file-input").val()}, function (res) {
        if(!res.success){
            alert(res.message);
            refreshHistoryView($("#editing-file-input").val());
        }
    });

    var timeId = setInterval(function () {
        $.post(BASE_PATH+"/files/gethistory.do", {fileId: $("#editing-file-input").val()}, function (res) {
            if (res.status == "SUCCESS" || res.status == "FAILED") {
                clearTimeout(timeId);
                $("#op-info").val("运行结束");
                $("#run-btn").removeAttr("disabled");
                refreshHistoryView($("#editing-file-input").val());
            }
            var cr = $("#log-p").html();
            var nr = res.log.replace(/\n/g, "<br>");
            if (cr != nr) {
                $("#log-p").html(nr);
                document.getElementById('console-div').scrollTop = document.getElementById('console-div').scrollHeight;
            }
        });

    }, 1000);
}

function saveContentBtnClick() {
	var newContent=editor.getValue();
    $.post(BASE_PATH+"/files/updatecontent.do", {content: newContent, fileId: $("#editing-file-input").val()}, function (res) {
        if (res) {
            $("#op-info").text("保存成功!");
        } else {
            $("#op-info").text("保存失败!");
        }
    });
}