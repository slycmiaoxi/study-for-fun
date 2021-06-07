package com.xxl.job.executor.domain;

import lombok.Data;


@Data
public class ResponseModel {
    /**
     * 返回结果
     */
    protected int result;

    /**
     * 返回总条数
     */
    protected int pagesize;

    /**
     * 消息
     */
    protected String message;


    /**
     * 当前页码
     */
    protected int pageindex;

    /**
     * 下页页码
     */
    protected Integer nextpage;

    /**
     * 错误码
     */
    protected int code;

    /**
     * 错误信息
     */
    protected String msg;

}
