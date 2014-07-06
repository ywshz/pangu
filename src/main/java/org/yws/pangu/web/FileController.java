package org.yws.pangu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.service.impl.FileServiceImpl;
import org.yws.pangu.utils.MemoryHelper;
import org.yws.pangu.web.webbean.FileTreeWebBean;
import org.yws.pangu.web.webbean.FileWebBean;
import org.yws.pangu.web.webbean.LogStatusWebBean;

@RequestMapping(value = "/files")
@Controller
public class FileController {

	private FileServiceImpl fileService = new FileServiceImpl();

	@RequestMapping(value = "list.do")
	public @ResponseBody
	FileTreeWebBean list(String id) {
		List<FileWebBean> l = new ArrayList<FileWebBean>();

		FileWebBean f1 = new FileWebBean("1", "root");
		FileWebBean f2 = new FileWebBean("2", "child 1", "file");

		l.add(f1);
		l.add(f2);

		return new FileTreeWebBean("个人文件", l);
	}

	@RequestMapping(value = "run.do")
	public @ResponseBody
	boolean run(String content) {

		try {
			return fileService.execute(content) == 0;
		} catch (IOException e) {
			return false;
		}
	}

	@RequestMapping(value = "getlog.do")
	public @ResponseBody
	LogStatusWebBean getLog(String jobId) {
		
		LogStatusWebBean wb = new LogStatusWebBean(MemoryHelper.JOB_STATUS_MAP.get(jobId),MemoryHelper.LOG_MAP.get(jobId).toString());
		
		MemoryHelper.LOG_MAP.remove("1");
		MemoryHelper.JOB_STATUS_MAP.remove("1");
		return wb;
	}
}