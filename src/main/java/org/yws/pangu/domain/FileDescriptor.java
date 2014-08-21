package org.yws.pangu.domain;

import javax.persistence.*;

import org.yws.pangu.enums.EFileType;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pangu_file")
public class FileDescriptor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column
    private String name;
    @Column
    private String owner;
    @Column
    private Integer parent;
    @Column
    private Short type;
    @Column
    private Long history;
    @Transient
    private boolean folder;
    @Column
    private String content;
    @Column(name = "gmt_create")
    private Date gmtCreate = new Date();
    @Column(name = "gmt_modified")
    private Date gmtModified = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public boolean isFolder() {
        return EFileType.FOLDER.isEqual(this.type);
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Long getHistory() {
        return history;
    }

    public void setHistory(Long history) {
        this.history = history;
    }
}
