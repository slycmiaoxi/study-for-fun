package com.xxl.job.executor.api;


public abstract class Factory {


    public final AbstractApiRequest create(String apiName) {
        AbstractApiRequest apiRequest = createApiRequest(apiName);
        return apiRequest;
    }


    protected abstract AbstractApiRequest createApiRequest(String apiName);



}
