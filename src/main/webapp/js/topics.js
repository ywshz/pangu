$(document).ready(function(){
	var pathName = window.document.location.pathname;
	var project = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    
	$("#searchBtn").click(function(){
		$("#topicTbody").html("<tr><td colspan='3'>加载中...</td></tr>");
		$.ajax({
		    url: project+"/topic/getAllTopics.do",
		    type: 'GET',
		    data: {zkList:$("#brokerListInput").val()},
		    dataType: 'json',
		    timeout: 15000,
		    error: function(){
		        alert('加载失败,请重试!');
		    },
		    success: function(res){
		    	$("#topicTbody").html("");
				$(res).each(function(index){
					var topic =  res[index];
					var topicName = topic.topic;
					var partitions = res[index].partitions;
					var partitionCount = partitions.length;
					
					var seeds="";
					$(partitions).each(function(index){
						var par = partitions[index];
						seeds+= par.id + ":" + par.seeds + " | ";
					});
					
					var tr = "<tr>"
							+"<td>"+topicName+"</td>"
							+"<td>"+partitionCount+"</td>"
							+"<td>"+seeds+"</td>"
							+ "</tr>";
					
					$("#topicTbody").append(tr);
				});
		    }
		});
		
	});
});