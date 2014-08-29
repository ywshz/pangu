package org.yws.pangu.rpc.slave;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.yws.pangu.rpc.slave.client.RegisterClient;

public class TaskNodeManager {
	private RegisterClient registerClient;
	private Thread heardBeatThread;
	public TaskNodeManager() {

	}

	public void start() {
		try {
			registerClient = new RegisterClient();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			InetAddress address = InetAddress.getLocalHost();
			final String localAddress = address.getHostAddress();
			final String localName = address.getHostName();
			registerClient.register(localAddress, localName);
			heardBeatThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(1000);
							registerClient.heartBeat(localAddress, localName);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			heardBeatThread.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		TaskNodeManager tm = new TaskNodeManager();
		tm.start();
	}

}
