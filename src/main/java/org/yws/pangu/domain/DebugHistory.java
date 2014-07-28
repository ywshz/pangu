package org.yws.pangu.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangshu.yang on 2014/7/28.
 */
@Entity
@Table(name = "pangu_debug_history")
public class DebugHistory implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="end_time")
    private Date endTime;
    @Column(name="start_time")
    private Date startTime;
    @Column(name = "file_id")
    private Integer fileId;
    @Column
    private String log;
    @Column(name="runtype")
    private String runType;
    @Column
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
