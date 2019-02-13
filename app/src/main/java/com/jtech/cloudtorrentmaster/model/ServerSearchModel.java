package com.jtech.cloudtorrentmaster.model;

/**
 * 服务器搜索站点对象
 */
public class ServerSearchModel extends BaseModel {
    private String name = "";
    private String keyword = "";

    public String getName() {
        return name;
    }

    public ServerSearchModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public ServerSearchModel setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}