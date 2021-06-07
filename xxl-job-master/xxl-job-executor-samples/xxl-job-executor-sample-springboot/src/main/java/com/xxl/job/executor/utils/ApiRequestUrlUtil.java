package com.xxl.job.executor.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;


public class ApiRequestUrlUtil {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestUrlUtil.class);

    public static HttpEntity<String> getHttpEntity(Map<String, Object> params, String name) {
        JSONObject jsonObject = new JSONObject();
        params.forEach(jsonObject::put);
        HttpHeaders headers = new HttpHeaders();
        MediaType jsonType = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(jsonType);
        JSONObject data = new JSONObject();
        data.put("data", jsonObject.toJSONString());
        logger.info(name + ":" + data.toJSONString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        return new HttpEntity<String>(data.toJSONString(), headers);
    }



    public static String getRequestUrl(String apiname, String token) {
        return apiname +
                "?token=" + token;
    }

}
