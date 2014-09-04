package org.yws.pangu.rpc.master;

import io.netty.channel.Channel;

import java.util.List;
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
	
	public void addTask(){
		
	}
	
	public void start(){
		try {
			registerServer = new RegisterServer(taskNodes);
			registerServer.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public List<TaskNode> getWokers(){
		return registerServer.getAllWokers();
	}
	
	public static void main(String[] args) throws InterruptedException {
		final MasterManager mm = new MasterManager();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					for(TaskNode node : mm.getWokers()){
						System.out.println(node.getHostAddress());
					}
					System.out.println("--");
				}	
			}
		}).start();
		mm.start();
		
	}
}
