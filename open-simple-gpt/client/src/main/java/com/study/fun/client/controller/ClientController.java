package com.study.fun.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.study.fun.client.enums.SystemCodeEnum;
import com.study.fun.client.model.ResponseDTO;
import com.study.fun.client.model.Result;
import com.study.fun.client.util.OpenHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/client")
public class ClientController {

    @Value("${client.open.appId}")
    private String appId;

    @Value("${client.open.appSecret}")
    private String appSecret;

    @Value("${client.open.path}")
    private String path;

    @GetMapping(value = "/test/gpt")
    public Result<String> testGpt(String text) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", "text-davinci-003");
        jsonObject.put("prompt", text);
        ResponseDTO responseDTO = null;
        try {
            responseDTO = OpenHttpUtil.post(appId, appSecret, path, jsonObject, new ArrayList<>(), null);
            if (responseDTO.getCode().equals(String.valueOf(SystemCodeEnum.K_000000))) {
                List<String> result = (List<String>) responseDTO.getData();
                return Result.success(result.toString());
            }
            return Result.fail(999,responseDTO.getMsg(),responseDTO.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success("success");
    }
}
