package com.study.fun.service.strategy.impl;


import com.study.fun.clickhouse.ClickHouseUtil;
import com.study.fun.domain.api.ApiLeesin;
import com.study.fun.service.strategy.AbstractDataStrategyService;

import java.util.List;
import java.util.Map;

public class CkDataStrategyServiceImpl extends AbstractDataStrategyService {
    @Override
    public List<?> listDatas() {
        String sql = "select * from api_leesin";
        List<Map<String, String>> maps = ClickHouseUtil.exeSql(sql);
        return maps;
    }

    @Override
    public int saveDate(List<ApiLeesin> list) {
        ClickHouseUtil.insertBatch(list,"api_leesin",ApiLeesin.class);
        return 1;
    }
}
