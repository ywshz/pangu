package org.yws.pangu.service;


public interface Job {
	Integer run() throws Exception;

	void cancel();

	JobContext getJobContext();

	boolean isCanceled();
}
