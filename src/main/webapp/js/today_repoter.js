$(document).ready(init);
function init() {

	var options = {
		chart : {
			renderTo : 'sf-chart',
			type : 'line'
		},
		title : {
			text : '任务运行趋势图',
			x : -20
		// center
		},
		subtitle : {
			text : '来源: Pangu',
			x : -20
		},
		xAxis : {
			categories : c
		},
		yAxis : {
			title : {
				text : '任务数(个)'
			},
			min : 0,
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ]
		},
		plotOptions : {
			line : {
				dataLabels : {
					enabled : true
				},
				enableMouseTracking : true
			}
		},
		tooltip : {
			valueSuffix : '个'
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'middle',
			borderWidth : 0
		},
		series : [ {
			name : '成功',
			data : s
		}, {
			name : '失败',
			data : f
		} ]
	};

	var chart = new Highcharts.Chart(options);

	// //////////////////////////////////////////
	
	var joboptions = {
		chart : {
			renderTo : 'job-costtime-chart',
			type : 'line'
		},
		title : {
			text : '核心任务耗时分析',
			x : -20
		// center
		},
		subtitle : {
			text : '来源: Pangu',
			x : -20
		},
		xAxis : {
			categories : []
		},
		yAxis : {
			title : {
				text : '耗时(秒)'
			},
			min : 0,
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ]
		},
		plotOptions : {
			line : {
				dataLabels : {
					enabled : true,
					formatter: function(){
						return Math.floor(this.y/60)+" min";
					}
				},
				enableMouseTracking : true
			}
		},
		tooltip : {
			valueSuffix : '秒'
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'middle',
			borderWidth : 0
		},
		series : [ {
			name : '流量基础报表',
			data : job16
		}, {
			name : '原始数据沉淀',
			data : job23
		}, {
			name : '基础数据沉淀',
			data : job24
		}]
	};

	var jobchart = new Highcharts.Chart(joboptions);

}