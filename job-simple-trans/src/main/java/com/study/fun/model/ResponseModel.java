package com.study.fun.model;

import lombok.Data;

/**
 * <p>
 * 顶层数据 返回类
 * </p>
 *
 */
@Data
public class ResponseModel {
    /**
     * 返回结果
     */
    protected int result;

    /**
     * 错误码
     */
    protected String errorcode;

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


}
