package org.yws.pangu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.domain.ResponseBean;
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
	public @ResponseBody Object list(Integer id) {
		List<FileWebBean> l = new ArrayList<FileWebBean>();

		// TODO:
		String owner = "1";

		if(id==null){
			//第一次进入目录
			FileBean fb = fileService.getFile(id, owner);
			if (fb.getSubFiles() != null && fb.getSubFiles().size() > 0) {
				for (FileDescriptor fd : fb.getSubFiles()) {
					l.add(new FileWebBean(fd.getId(), fd.getName(), fd.getType()));
				}
				return new FileTreeWebBean(fb.getFileDescriptor().getId().toString(), fb
						.getFileDescriptor().getName(), fb.getFileDescriptor().getType(), l);
			} else {
				return new FileTreeWebBean(fb.getFileDescriptor().getId().toString(), fb
						.getFileDescriptor().getName(), fb.getFileDescriptor().getType(), Collections.EMPTY_LIST);
			}
		}else{
			//非第一次
			FileBean fb = fileService.getFile(id, owner);
			for (FileDescriptor fd : fb.getSubFiles()) {
				l.add(new FileWebBean(fd.getId(), fd.getName(), fd.getType()));
			}
			return l;
		}
		

		// if (fb.getSubFiles() != null && fb.getSubFiles().size() > 0) {
		// for (FileDescriptor fd : fb.getSubFiles()) {
		// l.add(new FileWebBean(fd.getId(), fd.getName(), fd.getType()));
		// }
		// return new FileTreeWebBean(fb.getFileDescriptor().getId().toString(),
		// fb
		// .getFileDescriptor().getName(), fb.getFileDescriptor().getType(), l);
		// } else {
		// return new FileTreeWebBean(fb.getFileDescriptor().getId().toString(),
		// fb
		// .getFileDescriptor().getName(), fb.getFileDescriptor().getType(),
		// Collections.EMPTY_LIST);
		// }
	}

	@RequestMapping(value = "content.do")
	public @ResponseBody String getLog(Integer fileId) {
		// TODO:
		String owner = "1";
		return fileService.getContent(fileId, owner);
	}

	@RequestMapping(value = "updatecontent.do")
	public @ResponseBody boolean updatecontent(String content, Integer fileId) {
		// TODO:
		String owner = "1";
		return fileService.updateContent(fileId, content, owner);
	}

	@RequestMapping(value = "run.do")
	public @ResponseBody boolean run(Integer fileId, String content) {

		try {
			return fileService.execute(fileId, content) == 0;
		} catch (IOException e) {
			return false;
		}
	}

	@RequestMapping(value = "getlog.do")
	public @ResponseBody LogStatusWebBean getLog(String fileId) {

		LogStatusWebBean wb = new LogStatusWebBean(MemoryHelper.JOB_STATUS_MAP.get(fileId),
				MemoryHelper.LOG_MAP.get(fileId).toString());

		return wb;
	}

	@RequestMapping(value = "add.do")
	public @ResponseBody ResponseBean add(String name, String type, boolean isParent,
			Integer parentId) {
		System.out.println(name + "," + type + "," + parentId);

		// TODO:
		String owner = "1";

		FileDescriptor fd = new FileDescriptor();
		fd.setName(name);
		fd.setType((short) (isParent ? FileBean.FOLDER : FileBean.FILE));
		fd.setOwner(owner);
		fd.setParent(parentId);
		Integer id = fileService.save(fd);

		return new ResponseBean(true, id.toString());
	}

}