package com.xxl.job.executor.api.test;

import com.xxl.job.executor.api.AbstractApiRequest;
import com.xxl.job.executor.domain.ResponseModel;
import com.xxl.job.executor.domain.YurneroRequestModel;
import com.xxl.job.executor.enumerate.TaskEnumUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class YurneroApiRequest extends AbstractApiRequest {
    @Override
    public String initApiName() {
        return TaskEnumUtils.taskDatetimeType.YURNERO.key;
    }

    @Override
    public String initRelativeApiUrl() {
        String value = TaskEnumUtils.taskApiUrlType.getValueByKey(TaskEnumUtils.taskApiUrlType.YURNERO.key);
        return value;
    }

    @Override
    public Class<? extends ResponseModel> initMappingClass() {
        return YurneroRequestModel.class;
    }

    @Override
    public void insertOrUpdate(ResponseModel model) {
        YurneroRequestModel entity = (YurneroRequestModel) model;
        System.out.println(entity);

    }

    @Override
    public Map<String, Object> getRequestExtMap() {
        return null;
    }
}
