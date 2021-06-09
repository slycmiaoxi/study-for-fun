package com.study.fun.service.strategy;


import com.study.fun.domain.api.ApiLeesin;

import java.util.List;

public interface DataStrategyService {

    List<?> listDatas();

    int saveDate(List<ApiLeesin> list);
}
