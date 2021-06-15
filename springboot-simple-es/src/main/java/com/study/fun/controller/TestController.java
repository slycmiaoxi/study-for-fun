package com.study.fun.controller;


import com.alibaba.fastjson.JSON;
import com.study.fun.dto.EsParamDto;
import com.study.fun.es.EsUtil;
import com.study.fun.model.EsTestModel;
import com.study.fun.model.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {


    @RequestMapping("/init")
    public boolean init() {
        String index = "test";
        List<EsTestModel> list = new ArrayList<>();
        EsTestModel po = null;
        for (int i = 0; i < 10; i++) {
            po = new EsTestModel();
            po.setId(i + "");
            po.setUserName("admin" + i);
            po.setAge(i);
            list.add(po);
        }
        String paramListJson = JSON.toJSONString(list);
        boolean b = EsUtil.addBatch(index, "id", paramListJson, EsTestModel.class);
        return b;
    }

    @RequestMapping("/update")
    public boolean update() {
        String index = "test";
        String id = "0";
        EsTestModel po = new EsTestModel();
        po.setAge(100);
        po.setUserName("slycmiaoxi");
        String paramJson = JSON.toJSONString(po);
        boolean b = EsUtil.update(index, id, paramJson);
        return b;
    }

    @RequestMapping("/query")
    public String query() {
        EsParamDto dto = new EsParamDto();
        dto.setIsPage(true);
        dto.setTotal(3L);
        Map<String, Object> equalsSearchCondition = new HashMap<>();
        equalsSearchCondition.put("id", 1);
        dto.setEqualsSearchCondition(equalsSearchCondition);
        List<String> hightFieldList = new ArrayList<>();
        hightFieldList.add("age");
        dto.setHightFieldList(hightFieldList);
        PageResult result = EsUtil.query(dto, "test");
        return result.toString();
    }
}
