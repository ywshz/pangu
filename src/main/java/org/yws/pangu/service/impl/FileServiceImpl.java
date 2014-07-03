package org.yws.pangu.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

/*
 * 
 */
public class FileServiceImpl {
	public void execute(String content) throws IOException {
		// write file
		File file = File.createTempFile(UUID.randomUUID().toString(), ".hive");
		System.out.println(file.getAbsolutePath());
		file.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(content);
		out.close();

		// execute file

	}

	public static void main(String[] args) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
				"pwd");
//		builder.directory(new File("/tmp/"));
		builder.environment().putAll(System.getenv());
		Process process = builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();

		String threadName = null;
		// if(jobContext.getJobHistory()!=null &&
		// jobContext.getJobHistory().getJobId()!=null){
		// threadName="jobId="+jobContext.getJobHistory().getJobId();
		// }else if(jobContext.getDebugHistory()!=null &&
		// jobContext.getDebugHistory().getId()!=null){
		// threadName="debugId="+jobContext.getDebugHistory().getId();
		// }else{
		// threadName="not-normal-job";
		// }
		threadName = "text-job";
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} catch (Exception e) {
					System.out.println("接收日志出错，推出日志接收");
				}
			}
		}, threadName).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(errorStream));
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} catch (Exception e) {
					System.out.println("接收日志出错，推出日志接收");
				}
			}
		}, threadName).start();
		
		int exitCode = -999;
		try {
			exitCode = process.waitFor();
		} catch (InterruptedException e) {
			System.out.println(e);
		} finally{
			process=null;
		}
		if(exitCode!=0){
//			return exitCode;
		}
		
		
	}
}
