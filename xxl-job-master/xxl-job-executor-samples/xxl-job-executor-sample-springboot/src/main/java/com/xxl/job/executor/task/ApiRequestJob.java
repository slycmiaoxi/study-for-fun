package com.xxl.job.executor.task;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.api.AbstractApiRequest;
import com.xxl.job.executor.api.ApiRequestFactory;
import com.xxl.job.executor.enumerate.TaskEnumUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ApiRequestJob {
    @Resource
    private ApiRequestFactory factory;


    @XxlJob("leesinJobHandler")
    public ReturnT<String> leesinJobHandler(String param) throws Exception {
        try {
            AbstractApiRequest apiRequest = factory.create(TaskEnumUtils.taskDatetimeType.LEESIN.key);
            apiRequest.excute();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log(e);
        }
        return ReturnT.FAIL;
    }


    @XxlJob("yurneroJobHandler")
    public ReturnT<String> yurneroJobHandler(String param) throws Exception {
        try {
            AbstractApiRequest apiRequest = factory.create(TaskEnumUtils.taskDatetimeType.YURNERO.key);
            apiRequest.excute();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log(e);
        }
        return ReturnT.FAIL;
    }


}
