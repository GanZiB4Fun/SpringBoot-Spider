package com.bigwis.base;

import com.bigwis.util.StringUtils;

/**
 * Created by Administrator on 2017/3/10.
 */
public class BaseQuery {
    private int page = 1;
    private int rows = 10;
    private String sortColumns;
    private String sort;
    private String order;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSortColumns() {

        if (!StringUtils.isEmpty(order) && !StringUtils.isEmpty(sort)) {
            this.sortColumns = StringUtils.toUnderlineName(sort) + " " + order;
        }
        return sortColumns;
    }

    /**
     * 弃用该方法，使用该实体类时通过设置sort 和 order使用
     * @param sortColumns
     */
    @Deprecated
    public void setSortColumns(String sortColumns) {
        this.sortColumns = sortColumns;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
