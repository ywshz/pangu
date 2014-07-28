package org.yws.pangu.domain;

import org.yws.pangu.utils.MemoryDebugHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutputRedirector extends Thread {
	private InputStream is;
    private String jobId;

    public OutputRedirector(InputStream is, String jobId){
        this.is = is;
        this.jobId = jobId;
    }
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                MemoryDebugHelper.LOG_MAP.get(jobId).append(line+"\n");
            }
        } catch (IOException ioE) {
        	ioE.printStackTrace();
        }
    }
}
