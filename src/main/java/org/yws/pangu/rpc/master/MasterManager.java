package org.yws.pangu.rpc.master;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.yws.pangu.domain.TaskNode;
import org.yws.pangu.rpc.master.server.RegisterServer;
import org.yws.pangu.rpc.master.server.TaskReportServer;

public class MasterManager {

	private RegisterServer registerServer;
	private TaskReportServer taskReportServer;
	
	private Map<Channel, TaskNode> taskNodes=new ConcurrentHashMap<Channel, TaskNode>();
	
	public MasterManager(){
		
	}
	
	public void start(){
		try {
			registerServer = new RegisterServer(taskNodes);
			registerServer.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MasterManager mm = new MasterManager();
		mm.start();
	}
}
