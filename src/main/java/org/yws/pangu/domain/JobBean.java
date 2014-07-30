package org.yws.pangu.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pangu_job")
public class JobBean implements Serializable {
	public final static Integer AUTO = new Integer(1);
	public final static Integer MANUAL = new Integer(0);
	public final static String SHELL_JOB = "shell";
	public final static String HIVE_JOB = "hive";
	public final static Integer RUN_BY_TIME = new Integer(1);
	public final static Integer RUN_BY_DEPENDENCY = new Integer(0);
	

	/**  */
	private static final long serialVersionUID = -8367640586588912697L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column
	private Integer auto;
	@Column(name = "cron_expression")
	private String cron;
	@Column(name = "dependencies")
	private String dependencies;
	@Column(name = "descr")
	private String desc;
	@Column(name = "gmt_create")
	private Date gmtCreate = new Date();
	@Column(name = "gmt_modified")
	private Date gmtModified = new Date();
	@Column(name = "group_id")
	private Integer groupId;
	@Column(name = "history_id")
	private Long historyId;
	@Column
	private String name;
	@Column
	private String owner;
	@Column(name = "run_type")
	private String runType;
	@Column(name = "schedule_type")
	private Integer scheduleType;
	@Column(name = "script")
	private String script;
	@Column(name = "status")
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuto() {
		return auto;
	}

	public void setAuto(Integer auto) {
		this.auto = auto;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getDependencies() {
		return dependencies;
	}

	public void setDependencies(String dependencies) {
		this.dependencies = dependencies;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

}
