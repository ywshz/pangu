package org.yws.pangu.web;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.ResponseBean;
import org.yws.pangu.service.HdfsService;
import org.yws.pangu.service.impl.HdfsServiceImpl;


@RequestMapping(value = "/hdfs_browse")
@Controller
public class HdfsBrowseController {

    //@Autowired
    private HdfsService hdfsService = new HdfsServiceImpl();

    @RequestMapping(value = "list.do")
    public String list(String path, Model model) {

        if (path == null || path.trim().equals("")) {
            path = "/";
            model.addAttribute("current_path", "");
        }else{
            model.addAttribute("current_path", path);
        }

        model.addAttribute("files", hdfsService.getFiles(path));


        return "hdfs_browse";
    }
    
    @RequestMapping(value = "delete.do")
    public @ResponseBody ResponseBean delete(String path) {

        if (path == null || path.trim().equals("")) {
            return new ResponseBean(false,"Â·¾¶²»Õý³£");
        }

        try {
			if(hdfsService.delete(path)){
				return new ResponseBean(true,null);
			}else{
				return new ResponseBean(false,"É¾³ýÊ§°Ü,ÇëÖØÊÔ");
			}
		} catch (IOException e) {
			return new ResponseBean(false,"É¾³ýÊ§°Ü,"+e.getMessage());
		}

    }

}
