package com.xxl.job.executor.api.test;

import com.xxl.job.executor.api.AbstractApiRequest;
import com.xxl.job.executor.domain.LeesinResponseModel;
import com.xxl.job.executor.domain.ResponseModel;
import com.xxl.job.executor.enumerate.TaskEnumUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LeesinApiRequest extends AbstractApiRequest {
    @Override
    public String initApiName() {
        return TaskEnumUtils.taskDatetimeType.LEESIN.key;
    }

    @Override
    public String initRelativeApiUrl() {
        String value = TaskEnumUtils.taskApiUrlType.getValueByKey(TaskEnumUtils.taskApiUrlType.LEESIN.key);
        return value;
    }

    @Override
    public Class<? extends ResponseModel> initMappingClass() {
        return LeesinResponseModel.class;
    }

    @Override
    public void insertOrUpdate(ResponseModel model) {
        LeesinResponseModel entity = (LeesinResponseModel) model;
        System.out.println(entity);

    }

    @Override
    public Map<String, Object> getRequestExtMap() {
        return null;
    }
}
