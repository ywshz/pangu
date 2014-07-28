package org.yws.pangu.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.domain.DebugHistory;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.domain.ResponseBean;
import org.yws.pangu.service.impl.FileServiceImpl;
import org.yws.pangu.utils.MemoryDebugHelper;
import org.yws.pangu.web.webbean.DebugHistoryListItemWebBean;
import org.yws.pangu.web.webbean.FileTreeWebBean;
import org.yws.pangu.web.webbean.FileWebBean;
import org.yws.pangu.web.webbean.LogStatusWebBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestMapping(value = "/files")
@Controller
public class FileController {

    @Autowired
    private FileServiceImpl fileService;
    ;

    @RequestMapping(value = "list.do")
    public
    @ResponseBody
    Object list(Integer id) {
        List<FileWebBean> l = new ArrayList<FileWebBean>();

        // TODO:
        String owner = "1";

        if (id == null) {
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
        } else {
            //非第一次
            FileBean fb = fileService.getFile(id, owner);
            for (FileDescriptor fd : fb.getSubFiles()) {
                l.add(new FileWebBean(fd.getId(), fd.getName(), fd.getType()));
            }
            return l;
        }
    }

    @RequestMapping(value = "content.do")
    public
    @ResponseBody
    String getContent(Integer fileId) {
        // TODO:
        String owner = "1";
        return fileService.getContent(fileId, owner);
    }

    @RequestMapping(value = "updatecontent.do")
    public
    @ResponseBody
    boolean updateContent(String content, Integer fileId) {
        // TODO:
        String owner = "1";
        return fileService.updateContent(fileId, content, owner);
    }

    @RequestMapping(value = "run.do")
    public
    @ResponseBody
    Long run(Integer fileId, String content) {

        String owner = "1";
        try {
            //TODO:
            fileService.updateContent(fileId, content, owner);
            return fileService.execute(fileId, owner);
        } catch (IOException e) {
            return -1L;
        }
    }

    @RequestMapping(value = "add.do")
    public
    @ResponseBody
    ResponseBean add(String name, String type, boolean isParent,
                     Integer parentId) {
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

    @RequestMapping(value = "historylist.do", method = RequestMethod.POST)
    public
    @ResponseBody
    List<DebugHistoryListItemWebBean> historylist(Integer fileId) {
        List<DebugHistoryListItemWebBean> list = new ArrayList<DebugHistoryListItemWebBean>();

        for (DebugHistory his : fileService.getDebugHistoryList(fileId)) {
            list.add(new DebugHistoryListItemWebBean(his.getId().toString(), his.getStatus(), his.getStartTime(), his.getEndTime()));
        }

        return list;
    }

    @RequestMapping(value = "gethistory.do", method = RequestMethod.POST)
    public
    @ResponseBody
    LogStatusWebBean gethistory(Long historyId) {
        LogStatusWebBean wb ;
        DebugHistory his = fileService.getDebugHistory(historyId);
        if( "RUNNING".equals(his.getStatus()) && MemoryDebugHelper.JOB_STATUS_MAP.get(his.getFileId())!=null){
            wb = new LogStatusWebBean(MemoryDebugHelper.JOB_STATUS_MAP.get(his.getFileId()),
                    MemoryDebugHelper.LOG_MAP.get(his.getFileId()).toString());
        }else{
            wb = new LogStatusWebBean(his.getStatus(),his.getLog());
        }
        return wb;
    }

    @RequestMapping(value = "isrun.do", method = RequestMethod.POST)
    public
    @ResponseBody
    boolean isrun(Integer fileId) {

        String status = fileService.getDebugHistory(fileService.getFile(fileId, "1").getFileDescriptor().getHistory()).getStatus();

        return "RUNNING".equals(status) ? true : false;
    }

}