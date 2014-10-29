package org.yws.pangu.web.webbean;

import java.io.Serializable;

public class SearchResultWebBean implements Serializable {
    /**  */
    private static final long serialVersionUID = -7950003122611154715L;
    private String id;
    private String name;

    public SearchResultWebBean(String id, String name) {
        this.id = id;
        this.name = name;
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
}
