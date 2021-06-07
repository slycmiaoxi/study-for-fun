package com.xxl.job.executor.task;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.api.token.ApiTokenRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ApiTokenJob {

    @Resource
    private ApiTokenRequest apiTokenRequest;

    @XxlJob("tokenJobHandler")
    public ReturnT<String> tokenJobHandler(String param) throws Exception {
        try {
            apiTokenRequest.excute();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log(e);
        }
        return ReturnT.FAIL;
    }


}
