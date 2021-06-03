package com.study.fun.model;

import lombok.Data;

/**
 * <p>
 * 顶层数据 请求类
 * </p>
 *
 */
@Data
public class RequestModel {

    /**
     * 当前页码
     */
    protected int pageindex;

    /**
     * 开始时间
     */
    private String starttime;

    /**
     * 结束时间
     */
    private String endtime;
}
