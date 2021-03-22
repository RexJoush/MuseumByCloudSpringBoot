package com.nwu.entity;

/**
 * @author Rex Joush
 * @time 2021.03.22
 */

/**
 * 查询信息实体
 */
/*
    查询用户信息的类
 */
public class QueryInfo {

    private String query; // 查询信息
    private int pageStart; // 查询的起始页，即当前页
    private int pageSize; // 查询的分页大小

    public QueryInfo(String query, int pageStart, int pageSize) {
        this.query = query;
        this.pageStart = pageStart;
        this.pageSize = pageSize;
    }

    public QueryInfo() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageStart() {
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "QueryInfo{" +
                "query='" + query + '\'' +
                ", pageStart=" + pageStart +
                ", pageSize=" + pageSize +
                '}';
    }
}
