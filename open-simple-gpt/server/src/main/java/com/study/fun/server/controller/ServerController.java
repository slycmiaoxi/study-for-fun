package com.study.fun.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.study.fun.server.enums.SystemCodeEnum;
import com.study.fun.server.model.RequestDTO;
import com.study.fun.server.model.ResponseDTO;
import com.study.fun.server.util.OpenAiUtil;
import com.study.fun.server.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/server")
public class ServerController {

    private final RedisUtil redisUtil;

    //为了方便,暂且存在Map中
    private static Map<String, String> appMap = new HashMap<String, String>() {
        {
            put("123", "321");
        }
    };

    @PostMapping(value = "/chat/v1/completions")
    public ResponseDTO chatGpt(HttpServletRequest request, @RequestBody RequestDTO req) throws Exception {
        ResponseDTO responseDTO = new ResponseDTO();
        String param = req.getParam();
        if (StringUtils.isEmpty(param)) {
            responseDTO.setCode(String.valueOf(SystemCodeEnum.K_400003));
            responseDTO.setMsg("请求参数不合法！");
            return responseDTO;
        }


        JSONObject jsonObject = JSONObject.parseObject(param);
        Map<String, Object> innerMap = jsonObject.getInnerMap();
        //约定的提示语
        String prompt = (String) innerMap.get("prompt");
        if (checkOpenApiHeader(responseDTO, request)) {
            return responseDTO;
        }
        responseDTO.setCode(String.valueOf(SystemCodeEnum.K_000000));
        try {
            List<String> finalResult = new ArrayList<>();
            String chat = OpenAiUtil.sendChat(prompt, "user");
            log.info(chat);
            finalResult.add(chat);
            responseDTO.setMsg("success！");
            responseDTO.setData(finalResult);
        } catch (Exception e) {
            responseDTO.setMsg("验签错误！");
            log.error("消息异常,{}", e);
        }
        return responseDTO;

    }

    private boolean checkOpenApiHeader(ResponseDTO responseDTO, HttpServletRequest request) {
        String appId = request.getHeader("appId");
        String sign = request.getHeader("sign");
        String timeStamp = request.getHeader("timeStamp");
        String nonce = request.getHeader("nonce");
        // 请求时间有效期校验
        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if ((now - Long.parseLong(timeStamp)) / 1000 / 60 >= 5) {
            responseDTO.setCode(String.valueOf(SystemCodeEnum.K_400005));
            responseDTO.setMsg("请求过期！");
            return true;
        }
        String str = (String) redisUtil.get(appId + "_" + nonce);
        if (StringUtils.isNotEmpty(str)) {
            responseDTO.setCode(String.valueOf(SystemCodeEnum.K_400005));
            responseDTO.setMsg("请求失效！");
            return true;
        }

//        Map<Object, Object> appIdSecretMap = redisUtil.hmget("open_app");
//        String appSecret = (String) appIdSecretMap.getOrDefault(appId, "");
        String appSecret = appMap.get(appId);
        if (StringUtils.isEmpty(appSecret)) {
            responseDTO.setCode(String.valueOf(SystemCodeEnum.K_400002));
            responseDTO.setMsg("无接口访问权限！");
            return true;
        }

        redisUtil.set(appId + "_" + nonce, "1", 180L);
        String signStr = appSecret + appId + timeStamp + appSecret;
        String signServer = org.apache.commons.codec.digest.DigestUtils.md5Hex(signStr.getBytes());
        if (!signServer.equals(sign)) {
            responseDTO.setCode(String.valueOf(SystemCodeEnum.K_400001));
            responseDTO.setMsg("验签错误！");
            return true;
        }
        return false;
    }

}
