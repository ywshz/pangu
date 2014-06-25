package org.yws.pangu.web;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.HdfsFile;
import org.yws.pangu.service.HdfsService;
import org.yws.pangu.service.impl.HdfsServiceImpl;

import java.util.List;


@RequestMapping(value = "/hdfs_browse")
@Controller
public class HdfsBrowseController {

    //@Autowired
    private HdfsService hdfsService = new HdfsServiceImpl();

    @RequestMapping(value = "list.do")
    public String list(String path, Model model) {

        if (path == null || path.trim().equals("/")) {
            path = "/";
            model.addAttribute("current_path", "");
        }else{
            model.addAttribute("current_path", path);
        }

        model.addAttribute("files", hdfsService.getFiles(path));


        return "hdfs_browse";
    }

}
