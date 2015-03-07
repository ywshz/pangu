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
    <title>依赖图</title>
</head>

<body>

<canvas id="map" width="800" height="400"></canvas>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="${path }/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	var BASE_PATH='${path }';
    var jobId = '<%= request.getParameter("id")%>';
</script>
<script type="text/javascript" src="${path }/js/arbor.js"></script>
<script type="text/javascript" src="${path }/js/arbor-tween.js"></script>
<script type="text/javascript" src="${path }/js/arbor-graphics.js"></script>
<script type="text/javascript">
    (function($){
        var Renderer = function(canvas){
            var canvas = $(canvas).get(0)
            var ctx = canvas.getContext("2d");
            var gfx = arbor.Graphics(canvas)
            var particleSystem = null

            var that = {
                init:function(system){
                    particleSystem = system;
                    //particleSystem.screenSize(canvas.width, canvas.height)
                    var width = $(window).width();
                    var height = $(window).height();
                    particleSystem.screen({size:{width:width, height:height},
                        padding:[36,60,36,60]})
                    $(window).resize(that.resize)
                    that.resize();
                    that.initMouseHandling()
                },
                resize:function(){
                    var width = $(window).width();
                    var height = $(window).height();
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

        $(document).ready(function(){

            $.post(BASE_PATH + "/jobs/dependency_info.do", {id: jobId}, function (res) {

                var sys = arbor.ParticleSystem()
                sys.parameters({gravity:false, dt:0.01})
                sys.renderer = Renderer("#map")

//                var sys = arbor.ParticleSystem() // create the system with sensible repulsion/stiffness/friction
//                sys = arbor.ParticleSystem({ gravity: false, dt:0});
//                sys.renderer = Renderer("#viewport");

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
        })
    })(this.jQuery);






//    $(document).ready(function(){
//
//        $.post(BASE_PATH + "/jobs/dependency_info.do", {id: jobId}, function (res) {
//
//            var sys = arbor.ParticleSystem() // create the system with sensible repulsion/stiffness/friction
//            sys = arbor.ParticleSystem({ gravity: false, dt:0});
//            sys.renderer = Renderer("#viewport");
//
//            var nodes = res[0];
//            for (var i = 0, l = nodes.length; i < l; i++) {
//                var color = "green";
//                if (!nodes[i].lastRunSuccess)  color = "red";
//                sys.addNode(nodes[i].id, {
//                    color: color,
//                    'label': '[' + nodes[i].id + ']' + nodes[i].name,
//                    last_run_time: nodes[i].lastRun
//                })
//            }
//            var edges = res[1];
//            for (var i = 0, l = edges.length; i < l; i++) {
//                sys.addEdge(edges[i].son, edges[i].parent, {'directed': true});
//            }
//        });

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
</script>

</body>
</html>