package com.xxl.job.executor.config;


import com.xxl.job.executor.api.AbstractApiRequest;
import com.xxl.job.executor.api.ApiRequestFactory;
import com.xxl.job.executor.api.test.LeesinApiRequest;
import com.xxl.job.executor.api.test.YurneroApiRequest;
import com.xxl.job.executor.enumerate.TaskEnumUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SystemConfig {
    @Resource
    private LeesinApiRequest leesinApiRequest;
    @Resource
    private YurneroApiRequest yurneroApiRequest;

    @Bean
    public ApiRequestFactory createFactory() {
        ApiRequestFactory factory = new ApiRequestFactory();
        Map<String, AbstractApiRequest> serviceMap = new HashMap<>(6);
        serviceMap.put(TaskEnumUtils.taskDatetimeType.LEESIN.key, leesinApiRequest);
        serviceMap.put(TaskEnumUtils.taskDatetimeType.YURNERO.key, yurneroApiRequest);
        factory.setServiceMap(serviceMap);
        return factory;

    }


}
