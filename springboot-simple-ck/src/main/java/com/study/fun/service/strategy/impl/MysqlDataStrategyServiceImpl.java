package com.study.fun.service.strategy.impl;


import com.study.fun.domain.api.ApiLeesin;
import com.study.fun.service.api.IApiLeesinService;
import com.study.fun.service.strategy.AbstractDataStrategyService;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class MysqlDataStrategyServiceImpl extends AbstractDataStrategyService {
    private final IApiLeesinService apiLeesinService;

    public MysqlDataStrategyServiceImpl(final IApiLeesinService apiLeesinService) {
        this.apiLeesinService = apiLeesinService;
    }


    @Override
    public List<?> listDatas() {
        return apiLeesinService.list();
    }

    @Override
    public int saveDate(List<ApiLeesin> list) {
        if (!CollectionUtils.isEmpty(list)) {
            apiLeesinService.saveBatch(list);
        }
        return 1;
    }
}
