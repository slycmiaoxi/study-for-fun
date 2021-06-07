package com.xxl.job.executor.api;


import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


public class ApiRequestFactory extends Factory {
    /**
     * 保存注入的所有ApiXXXRequest
     */
    private Map<String, AbstractApiRequest> serviceMap;


    @Override
    protected AbstractApiRequest createApiRequest(String apiName) {
        if (StringUtils.isEmpty(apiName) || null == this.serviceMap) {
            XxlJobLogger.log("初始化AbstractApiRequest失败");
            return null;
        }
        return serviceMap.get(apiName);
    }



    public void setServiceMap(Map<String, AbstractApiRequest> serviceMap) {
        this.serviceMap = serviceMap;
    }

}
