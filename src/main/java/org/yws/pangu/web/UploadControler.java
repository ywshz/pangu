package org.yws.pangu.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.yws.pangu.service.HdfsService;
import org.yws.pangu.service.impl.HdfsServiceImpl;

@RequestMapping(value = "/upload")
@Controller
public class UploadControler {
	// @Autowired
	private HdfsService hdfsService = new HdfsServiceImpl();
	private static final String BASE_UPLOAD_PATH = "/user/pangu/uploads";
	private static final String FILE_TYPE = ".zip";

	@RequestMapping(value = "/uploadFile.do", method = RequestMethod.POST)
	public @ResponseBody boolean uploadFile(
			@RequestParam(value = "file", required = false) MultipartFile file, Integer jobId)
			throws IOException {
		try {
			hdfsService.upload(BASE_UPLOAD_PATH + "/" + jobId + FILE_TYPE, file.getBytes());
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}

	@RequestMapping(value = "/exist.do", method = RequestMethod.POST)
	public @ResponseBody String exist(Integer jobId) throws IOException {
		if(hdfsService.isExist(BASE_UPLOAD_PATH + "/" + jobId + FILE_TYPE)){
			return BASE_UPLOAD_PATH + "/" + jobId + FILE_TYPE;
		}
		return "未上传";
	}
}
