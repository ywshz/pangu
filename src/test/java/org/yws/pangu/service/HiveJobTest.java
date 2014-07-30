package org.yws.pangu.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.yws.pangu.utils.MemoryDebugHelper;

public class HiveJobTest {

	public static void main(String[] args) throws IOException, InterruptedException {

		ProcessBuilder builder = new ProcessBuilder("hive", "-e", "show tabs;");
		Process process = builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();

		Thread t1 = new Thread() {
			@Override
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						System.out.println("INFO:>" + line + "/n");
					}
				} catch (IOException ioE) {
					ioE.printStackTrace();
				}
			}
		};
		Thread t2 = new Thread() {
			@Override
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(errorStream);
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					while ((line = br.readLine()) != null) {
						System.out.println("ERROR:>" + line + "/n");
					}
				} catch (IOException ioE) {
					ioE.printStackTrace();
				}
			}
		};

		t1.start();
		t2.start();

		process.waitFor();

	}
}
