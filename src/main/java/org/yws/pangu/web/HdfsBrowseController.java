package org.yws.pangu.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.ResponseBean;
import org.yws.pangu.service.HdfsService;
import org.yws.pangu.service.impl.HdfsServiceImpl;

@RequestMapping(value = "/hdfs_browse")
@Controller
public class HdfsBrowseController {

	// @Autowired
	private HdfsService hdfsService = new HdfsServiceImpl();

	@RequestMapping(value = "list.do")
	public String list(String path, Model model) {

		if (path == null || path.trim().equals("")) {
			path = "/";
			model.addAttribute("current_path", "/");
		} else {
			path = path.trim();
			if (path.endsWith("/")) {
				model.addAttribute("current_path", path);
				String p = path.substring(0, path.length() - 1);
				model.addAttribute("parent_path", p.substring(0, p.lastIndexOf("/") + 1));
			} else {
				model.addAttribute("current_path", path + "/");
				model.addAttribute("parent_path", path.substring(0, path.lastIndexOf("/") + 1));
			}

		}

		model.addAttribute("files", hdfsService.getFiles(path));

		return "hdfs_browse";
	}

	@RequestMapping(value = "delete.do")
	public @ResponseBody
	ResponseBean delete(String path) {

		if (path == null || path.trim().equals("")) {
			return new ResponseBean(false, "¬∑æ∂≤ª’˝≥£");
		}

		try {
			if (hdfsService.delete(path)) {
				return new ResponseBean(true, null);
			} else {
				return new ResponseBean(false, "…æ≥˝ ß∞‹,«Î÷ÿ ‘");
			}
		} catch (IOException e) {
			return new ResponseBean(false, "…æ≥˝ ß∞‹," + e.getMessage());
		}

	}

	@RequestMapping(value = "rename.do")
	public @ResponseBody
	ResponseBean rename(@RequestParam(required = true) String src,
			@RequestParam(required = true) String dst) {
		dst = dst.replaceAll(" ", "_");
		try {
			if (hdfsService.rename(src, dst)) {
				return new ResponseBean(true, null);
			} else {
				return new ResponseBean(false, "“∆∂Ø ß∞‹,«Î÷ÿ ‘");
			}
		} catch (Exception e) {
			return new ResponseBean(false, "“∆∂Ø ß∞‹," + e.getMessage());
		}

	}

}
