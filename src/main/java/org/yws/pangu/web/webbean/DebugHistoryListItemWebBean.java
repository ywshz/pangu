package org.yws.pangu.web.webbean;

import org.yws.pangu.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangshu.yang on 2014/7/28.
 */
public class DebugHistoryListItemWebBean implements Serializable {

    private String id;
    private String status;
    private Date startTime;
    private Date endTime;

    public DebugHistoryListItemWebBean(String id, String status, Date startTime, Date endTime) {
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEndTime() {
        return DateUtils.format(endTime.getTime(),"yyyy/MM/dd HH:mm:ss");
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return DateUtils.format(startTime.getTime(),"yyyy/MM/dd HH:mm:ss");
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
