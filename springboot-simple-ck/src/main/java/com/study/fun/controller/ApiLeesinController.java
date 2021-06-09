package com.study.fun.controller;


import com.study.fun.domain.api.ApiLeesin;
import com.study.fun.service.strategy.DataStrategyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/apiLeesin")
public class ApiLeesinController {
    @Resource
    private DataStrategyService dataStrategyService;

    @ResponseBody
    @RequestMapping("/listTest")
    public String test() {
        List<?> list = dataStrategyService.listDatas();
        return list.toString();
    }

    @ResponseBody
    @RequestMapping("/saveTest")
    public String saveTest() {
        List<ApiLeesin> list = new ArrayList<>();
        ApiLeesin po;
        for (int i = 5; i < 10; i++) {
            po = new ApiLeesin();
            po.setId(i);
            po.setApiTest1("test" + i);
            po.setApiTest2("test" + i);
            list.add(po);
        }

        dataStrategyService.saveDate(list);
        List<?> testList = dataStrategyService.listDatas();
        return testList.toString();
    }



}

