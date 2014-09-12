package org.yws.pangu.service;

public class Demo1 {

	public static void main(String[] args) {
		String s ="{\"platform\":0,\"sessionId\":\"51b28113154f479789d3a9a8a855c5bb\",\"sdkVer\":0,\"screenWidth\":0,\"screenHeight\":0,\"dpi\":0,\"operator\":0,\"clientType\":0,\"clientVer\":0,\"pdCode\":\"mopote_12000003\",\"secChannelCode\":\"2_zjydcmcc_045770500001_\",\"clientHost\":\"122.224.212.136\",\"accessHost\":\"192.168.100.46\",\"accessPort\":8888,\"localHost\":\"192.168.100.46\",\"localPort\":10000,\"createTime\":\"2014-09-03 15:49:26\",\"bizCode\":2000}";
		System.out.println(new String(s.getBytes()));
		
		System.out.println(false == Boolean.FALSE);
	}

}
