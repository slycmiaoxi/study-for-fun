package com.study.fun.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    /**
     * 数据总条数
     */
    private Long total;

    /**
     * 当前页
     */
    private int page;

    /**
     * 每页大小
     */
    private int size;


    /**
     * 数据
     */
    private List<T> rows;


}
