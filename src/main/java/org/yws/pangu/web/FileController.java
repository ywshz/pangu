package org.yws.pangu.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.web.webbean.FileTreeWebBean;
import org.yws.pangu.web.webbean.FileWebBean;

@RequestMapping(value = "/files")
@Controller
public class FileController {


	@RequestMapping(value = "list.do")
	public @ResponseBody
	FileTreeWebBean list(String id) {
		List<FileWebBean> l = new ArrayList<FileWebBean>();
		
		FileWebBean f1 = new FileWebBean("1","root");
		FileWebBean f2 = new FileWebBean("2","child 1","file");
		
		l.add(f1);
		l.add(f2);
		
		return new FileTreeWebBean("个人文件",l);
	}


}