package org.yws.pangu.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogOutputRedirector extends Thread {
	private static Logger LOGGER = LoggerFactory.getLogger(LogOutputRedirector.class);
	private InputStream is;

	public LogOutputRedirector(InputStream is) {
		this.is = is;
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				LOGGER.info(line);
			}
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}
	}
}
