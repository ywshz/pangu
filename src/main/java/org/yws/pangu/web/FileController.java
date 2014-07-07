package org.yws.pangu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.service.impl.FileServiceImpl;
import org.yws.pangu.utils.MemoryHelper;
import org.yws.pangu.web.webbean.FileTreeWebBean;
import org.yws.pangu.web.webbean.FileWebBean;
import org.yws.pangu.web.webbean.LogStatusWebBean;

@RequestMapping(value = "/files")
@Controller
public class FileController {

	@Autowired
	private FileServiceImpl fileService;;

	@RequestMapping(value = "list.do")
	public @ResponseBody
	FileTreeWebBean list(Integer id) {
		List<FileWebBean> l = new ArrayList<FileWebBean>();

		//TODO:
		String owner = "1";
		
		FileBean fb = fileService.getFile(id, owner);
		
		if(fb.getSubFiles()!=null){
			for(FileDescriptor fd : fb.getSubFiles()){
				l.add(new FileWebBean(fd.getId(), fd.getName(), fd.isFolder()));
			}
		}
		
		return new FileTreeWebBean(fb.getFileDescriptor().getName(), l);
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
		
		return wb;
	}
}