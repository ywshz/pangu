package org.yws.pangu.utils;

import java.util.HashMap;
import java.util.Map;

public class JobExecutionMemoryHelper {
	public static Map<Long, StringBuffer> jobLogMemoryHelper = new HashMap<Long, StringBuffer>();
	public static Map<Long, String> jobStatusMemoryHelper = new HashMap<Long, String>();
	public static final String RUNNING = "RUNNING";
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
}
