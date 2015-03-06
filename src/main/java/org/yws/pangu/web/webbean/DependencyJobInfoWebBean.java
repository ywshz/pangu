package org.yws.pangu.web.webbean;

import java.io.Serializable;

/**
 * Created by wangshu.yang on 2015/3/6.
 */
public class DependencyJobInfoWebBean  implements Serializable {
    private String id;
    private String name;
    private String lastRun;
    private boolean lastRunSuccess;

    public DependencyJobInfoWebBean() {
    }

    public DependencyJobInfoWebBean(String id, String name, String lastRun, boolean lastRunSuccess) {
        this.id = id;
        this.name = name;
        this.lastRun = lastRun;
        this.lastRunSuccess = lastRunSuccess;
    }

    public boolean isLastRunSuccess() {
        return lastRunSuccess;
    }

    public void setLastRunSuccess(boolean lastRunSuccess) {
        this.lastRunSuccess = lastRunSuccess;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastRun() {
        return lastRun;
    }

    public void setLastRun(String lastRun) {
        this.lastRun = lastRun;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DependencyJobInfoWebBean)) return false;

        DependencyJobInfoWebBean that = (DependencyJobInfoWebBean) o;

        if (lastRunSuccess != that.lastRunSuccess) return false;
        if (!id.equals(that.id)) return false;
        if (!lastRun.equals(that.lastRun)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + lastRun.hashCode();
        result = 31 * result + (lastRunSuccess ? 1 : 0);
        return result;
    }
}
