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

function Renderer(canvas){
        var canvas = $(canvas).get(0)
        var ctx = canvas.getContext("2d");
        var gfx = arbor.Graphics(canvas)
        var particleSystem = null

        var that = {
            init:function(system){
                particleSystem = system;
                //particleSystem.screenSize(canvas.width, canvas.height)
                var width = $(".dependency-div").width()*0.95;
                var height = 400;
                particleSystem.screen({size:{width:width, height:height},
                    padding:[36,60,36,60]})
                $(window).resize(that.resize)
                that.resize();
                that.initMouseHandling()
            },
            resize:function(){
                var width = $(".dependency-div").width()*0.95;
                var height = 400;
                canvas.width = width;
                canvas.height = height;
                particleSystem.screen({size:{width:canvas.width, height:canvas.height}})
                that.redraw()
            },
            redraw:function(){
                gfx.clear();
                var nodeBoxes = {}
                particleSystem.eachNode(function(node, pt){
                    var label = node.data.label||"";
                    var last_run_time = node.data.last_run_time||"";
                    var w = Math.max(ctx.measureText(""+label).width,ctx.measureText(""+last_run_time).width) + 20;
                    if (!(""+label).match(/^[ \t]*$/)){
                        pt.x = Math.floor(pt.x)
                        pt.y = Math.floor(pt.y)
                    }else{
                        label = null
                    }
                    gfx.rect(pt.x-w/2, pt.y-8, w, 36, 4, {fill:node.data.color, alpha:node.data.alpha});
                    gfx.text(node.data.label, pt.x, pt.y+9, {color:"black", align:"center", font:"Arial", size:12});
                    gfx.text(node.data.last_run_time, pt.x, pt.y+9+14, {color:"black", align:"center", font:"Arial", size:12});
                    nodeBoxes[node.name] = [pt.x-w/2, pt.y-11, w, 38];
                });
                // draw the edges
                particleSystem.eachEdge(function(edge, pt1, pt2){
                    if (edge.source.data.alpha * edge.target.data.alpha == 0) return

                    var weight = edge.data.weight
                    var color = edge.data.color

                    if (!color || (""+color).match(/^[ \t]*$/)) color = null

                    // find the start point
                    var tail = intersect_line_box(pt1, pt2, nodeBoxes[edge.source.name])
                    var head = intersect_line_box(tail, pt2, nodeBoxes[edge.target.name])

                    gfx.line(tail, head, {stroke:"#333333", width:1, alpha:edge.target.data.alpha})

                    // draw an arrowhead if this is a -> style edge
                    if (edge.data.directed){
                        ctx.save()
                        // move to the head position of the edge we just drew
                        var wt = !isNaN(weight) ? parseFloat(weight) : 1
                        var arrowLength = 6 + wt
                        var arrowWidth = 2 + wt
                        ctx.fillStyle = (color) ? color : "#333333"
                        ctx.translate(head.x, head.y);
                        ctx.rotate(Math.atan2(head.y - tail.y, head.x - tail.x));

                        // delete some of the edge that's already there (so the point isn't hidden)
                        ctx.clearRect(-arrowLength/2,-wt/2, arrowLength/2,wt)

                        // draw the chevron
                        ctx.beginPath();
                        ctx.moveTo(-arrowLength, arrowWidth);
                        ctx.lineTo(0, 0);
                        ctx.lineTo(-arrowLength, -arrowWidth);
                        ctx.lineTo(-arrowLength * 0.8, -0);
                        ctx.closePath();
                        ctx.fill();
                        ctx.restore()
                    }
                })
            },
            initMouseHandling:function(){
                // no-nonsense drag and drop (thanks springy.js)
                selected = null;
                nearest = null;
                var dragged = null;
                var oldmass = 1

                // set up a handler object that will initially listen for mousedowns then
                // for moves and mouseups while dragging
                var handler = {
                    clicked:function(e){
                        var pos = $(canvas).offset();
                        _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
                        selected = nearest = dragged = particleSystem.nearest(_mouseP);

                        if (dragged.node !== null) dragged.node.fixed = true

                        $(canvas).bind('mousemove', handler.dragged)
                        $(window).bind('mouseup', handler.dropped)

                        return false
                    },
                    dragged:function(e){
                        var old_nearest = nearest && nearest.node._id
                        var pos = $(canvas).offset();
                        var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

                        if (!nearest) return
                        if (dragged !== null && dragged.node !== null){
                            var p = particleSystem.fromScreen(s)
                            dragged.node.p = p
                        }

                        return false
                    },

                    dropped:function(e){
                        if (dragged===null || dragged.node===undefined) return
                        if (dragged.node !== null) dragged.node.fixed = false
                        dragged.node.tempMass = 50
                        dragged = null
                        selected = null
                        $(canvas).unbind('mousemove', handler.dragged)
                        $(window).unbind('mouseup', handler.dropped)
                        _mouseP = null
                        return false
                    }
                }
                $(canvas).mousedown(handler.clicked);

            }

        }

        // helpers for figuring out where to draw arrows (thanks springy.js)
        var intersect_line_line = function(p1, p2, p3, p4)
        {
            var denom = ((p4.y - p3.y)*(p2.x - p1.x) - (p4.x - p3.x)*(p2.y - p1.y));
            if (denom === 0) return false // lines are parallel
            var ua = ((p4.x - p3.x)*(p1.y - p3.y) - (p4.y - p3.y)*(p1.x - p3.x)) / denom;
            var ub = ((p2.x - p1.x)*(p1.y - p3.y) - (p2.y - p1.y)*(p1.x - p3.x)) / denom;

            if (ua < 0 || ua > 1 || ub < 0 || ub > 1)  return false
            return arbor.Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
        }

        var intersect_line_box = function(p1, p2, boxTuple)
        {
            var p3 = {x:boxTuple[0], y:boxTuple[1]},
                w = boxTuple[2],
                h = boxTuple[3]

            var tl = {x: p3.x, y: p3.y};
            var tr = {x: p3.x + w, y: p3.y};
            var bl = {x: p3.x, y: p3.y + h};
            var br = {x: p3.x + w, y: p3.y + h};

            return intersect_line_line(p1, p2, tl, tr) ||
                intersect_line_line(p1, p2, tr, br) ||
                intersect_line_line(p1, p2, br, bl) ||
                intersect_line_line(p1, p2, bl, tl) ||
                false
        }

        return that
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

    //依赖图按钮
    var sys = arbor.ParticleSystem() // create the system with sensible repulsion/stiffness/friction
    sys = arbor.ParticleSystem({ gravity: false, dt:0});
    sys.renderer = Renderer("#viewport");
    $("#dependency-btn").click(function () {

        $('#dependencyModal').modal({
            backdrop: 'static',
            keyboard: false
        });
        $('#dependencyModal').on('shown.bs.modal', function (e) {

            $.post(BASE_PATH + "/jobs/dependency_info.do", {id: $("#viewing-job-input").val()}, function (res) {

                sys.eachNode(function(node){
                    sys.pruneNode(node);
                });
                var nodes = res[0];
                for (var i = 0, l = nodes.length; i < l; i++) {
                    var color = "green";
                    if (!nodes[i].lastRunSuccess)  color = "red";
                    sys.addNode(nodes[i].id, {
                        color: color,
                        'label': '[' + nodes[i].id + ']' + nodes[i].name,
                        last_run_time: nodes[i].lastRun
                    })
                }
                var edges = res[1];
                for (var i = 0, l = edges.length; i < l; i++) {
                    sys.addEdge(edges[i].son, edges[i].parent, {'directed': true});
                }
            });

            // add some nodes to the graph and watch it go...
            //sys.addNode('a',{color:'green','label':'任务1 <br/>',last_run_time:'2015-02-02 12:12:12'})
            //sys.addNode('b',{color:'green','label':'监控。。。 ',last_run_time:'2015-02-02 12:12:12'})
            //sys.addNode('c',{color:'green','label':'啥啊 2',last_run_time:'2015-02-02 12:12:12'})
            //sys.addNode('d',{color:'green','label':'没理由 20',last_run_time:'2015-02-02 12:12:12'})
            //sys.addNode('f',{color:'red','label':'不是吧 20',last_run_time:'2015-02-02 12:12:12'})
            //
            //sys.addEdge('a','b',{'shape':'dot','directed':true})
            //sys.addEdge('b','c',{'shape':'dot','label':'123','directed':true})
            //sys.addEdge('b','d',{'shape':'dot','label':'123','directed':true})
            //sys.addEdge('d','f',{'shape':'dot','label':'123','directed':true})
        });
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