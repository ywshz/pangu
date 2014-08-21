package org.yws.pangu.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.DebugHistory;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.domain.ResponseBean;
import org.yws.pangu.enums.EFileType;
import org.yws.pangu.service.impl.FileServiceImpl;
import org.yws.pangu.utils.MemoryDebugHelper;
import org.yws.pangu.web.webbean.DebugHistoryListItemWebBean;
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

		if (id == null) {
			// 第一次进入目录
			FileBean fb = fileService.getFile(id, owner);
			if (fb.getSubFiles() != null && fb.getSubFiles().size() > 0) {
				for (FileDescriptor fd : fb.getSubFiles()) {
					l.add(new FileWebBean(fd.getId(), fd.getName(), fd
							.getType()));
				}
				return new FileTreeWebBean(fb.getFileDescriptor().getId()
						.toString(), fb.getFileDescriptor().getName(), fb
						.getFileDescriptor().getType(), l);
			} else {
				return new FileTreeWebBean(fb.getFileDescriptor().getId()
						.toString(), fb.getFileDescriptor().getName(), fb
						.getFileDescriptor().getType(), Collections.EMPTY_LIST);
			}
		} else {
			// 非第一次
			FileBean fb = fileService.getFile(id, owner);
			for (FileDescriptor fd : fb.getSubFiles()) {
				l.add(new FileWebBean(fd.getId(), fd.getName(), fd.getType()));
			}
			return l;
		}
	}

	@RequestMapping(value = "content.do")
	public @ResponseBody String getContent(Integer fileId) {
		// TODO:
		String owner = "1";
		return fileService.getContent(fileId, owner);
	}

	@RequestMapping(value = "updatecontent.do")
	public @ResponseBody boolean updateContent(String content, Integer fileId) {
		// TODO:
		String owner = "1";
		return fileService.updateContent(fileId, content, owner);
	}

	@RequestMapping(value = "updatename.do")
	public @ResponseBody boolean updatename(Integer id, String name) {
		// TODO:
		String owner = "1";
		return fileService.updateName(id, name, owner);
	}

	@RequestMapping(value = "delete.do")
	public @ResponseBody boolean delete(Integer id) {
		// TODO:
		String owner = "1";
		if(fileService.getFile(id, owner).getSubFiles().size()>0){
			return false;
		}else{
			fileService.delete(id, owner);
			return true;
		}
	}

	@RequestMapping(value = "add.do")
	public @ResponseBody ResponseBean add(String name, String type,
			boolean isParent, Integer parentId) {
		// TODO:
		String owner = "1";

		FileDescriptor fd = new FileDescriptor();
		fd.setName(name);
		fd.setType((isParent ? EFileType.FOLDER.getValue() : EFileType.FILE.getValue()));
		fd.setOwner(owner);
		fd.setParent(parentId);
		Integer id = fileService.save(fd);

		return new ResponseBean(true, id.toString());
	}

	@RequestMapping(value = "historylist.do", method = RequestMethod.POST)
	public @ResponseBody List<DebugHistoryListItemWebBean> historylist(
			Integer fileId) {
		List<DebugHistoryListItemWebBean> list = new ArrayList<DebugHistoryListItemWebBean>();

		for (DebugHistory his : fileService.getDebugHistoryList(fileId)) {
			list.add(new DebugHistoryListItemWebBean(his.getId().toString(),
					his.getStatus(), his.getStartTime(), his.getEndTime()));
		}

		return list;
	}

	@RequestMapping(value = "run.do")
	public @ResponseBody ResponseBean run(Integer fileId, String content) {

		String owner = "1";
		try {
			// TODO:
			fileService.updateContent(fileId, content, owner);
			int res = fileService.execute(fileId, owner);
			if (res == 0) {
				return new ResponseBean(true);
			} else if (res == -2) {
				return new ResponseBean(false,
						"This job is still running, please wait!");
			}
		} catch (IOException e) {
			return new ResponseBean(false, "运行时发生异常." + e.getMessage());
		}
		return new ResponseBean(true);
	}

	@RequestMapping(value = "gethistory.do", method = RequestMethod.POST)
	public @ResponseBody LogStatusWebBean gethistoryByFileId(Integer fileId) {

		LogStatusWebBean wb;
		String jobStatus = MemoryDebugHelper.JOB_STATUS_MAP.get(fileId
				.toString());
		if (jobStatus == null) {
			Long hisId = fileService.getFile(fileId, "1").getFileDescriptor()
					.getHistory();
			DebugHistory his = fileService.getDebugHistory(hisId);
			wb = new LogStatusWebBean(his.getStatus(), his.getLog());
		} else {
			wb = new LogStatusWebBean(
					MemoryDebugHelper.JOB_STATUS_MAP.get(fileId.toString()),
					MemoryDebugHelper.LOG_MAP.get(fileId.toString()).toString());
		}
		// System.out.println("######gethistoryByFileId"+fileId);
		//
		// if (hisId == null) {
		// System.out.println("######hisId is null");
		// wb = new LogStatusWebBean("RUNNING", "");
		// } else {
		// DebugHistory his = fileService.getDebugHistory(hisId);
		// if(his.getLog()==null){
		// System.out.println("######Read from memory");
		// wb = new
		// LogStatusWebBean(MemoryDebugHelper.JOB_STATUS_MAP.get(his.getFileId().toString()),
		// MemoryDebugHelper.LOG_MAP.get(his.getFileId().toString()).toString());
		// }else{
		// System.out.println("######Read from db");
		// wb = new LogStatusWebBean(his.getStatus(), his.getLog());
		// }
		// }
		return wb;
	}

	@RequestMapping(value = "gethistorylog.do", method = RequestMethod.POST)
	public @ResponseBody String gethistorylog(Long id) {
		return fileService.getDebugHistory(id).getLog();
	}
}