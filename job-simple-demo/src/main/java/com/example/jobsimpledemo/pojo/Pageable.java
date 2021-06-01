package com.example.jobsimpledemo.pojo;

import com.github.pagehelper.Page;

import java.io.Serializable;


public class Pageable implements Serializable {

    /**
     * 当前页
     */
    private long page;

    /**
     * 总页数
     */
    private long total;

    /**
     * 页面条数
     */
    private long pageSize;

    /**
     * 总条数
     */
    private long records;

    /**
     * 数据
     */
    private Object rows;


    private Pageable(long page, long total, long pageSize, long records, Object data) {
        this.page = page;
        this.total = total;
        this.pageSize = pageSize;
        this.records = records;
        this.rows = data;
    }

    public static PageableBuilder builder() {
        return new PageableBuilder();
    }

    public long getPage() {
        return page;
    }

    public long getTotal() {
        return total;
    }


    public long getRecords() {
        return records;
    }


    @Override
    public String toString() {
        return "Pageable{" +
                "page=" + page +
                ", total=" + total +
                ", pageSize=" + pageSize +
                ", records=" + records +
                ", rows=" + rows +
                '}';
    }

    public Object getRows() {
        return rows;
    }


    public static class PageableBuilder<T> {
        private int page;

        private int total;

        private int pageSize;

        private long records;

        private Object rows;

        PageableBuilder() {
        }

        public PageableBuilder page(int page) {
            this.page = page;
            return this;
        }

        public PageableBuilder total(int total) {
            this.total = total;
            return this;
        }

        public PageableBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PageableBuilder records(int records) {
            this.records = records;
            return this;
        }

        public PageableBuilder data(Object rows) {
            this.rows = rows;
            return this;
        }

        public PageableBuilder<T> rows(T rows) {
            this.rows = rows;
            return this;
        }

        /**
         * 构造jqgrid实体
         *
         * @param page
         * @return
         */
        public Pageable buildForJqGrid(Page page) {
            return new Pageable(page.getPageNum(), page.getPages(), page.getPageSize(), page.getTotal(), page.getResult());
        }

        public Pageable build() {
            return new Pageable(this.page, this.total, this.pageSize, this.records, this.rows);
        }

    }


}
