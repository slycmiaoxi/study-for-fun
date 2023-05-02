package com.study.fun.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.study.fun.client.model.RequestDTO;
import com.study.fun.client.model.ResponseDTO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class OpenHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(OpenHttpUtil.class);
    private static final String defaultCharset = "UTF-8";
    private static final String defaultContentType = "application/json;charset=UTF-8";
    private static final String MULTIPART_FORM = "multipart/form-data";

    public OpenHttpUtil() {
    }

    public static ResponseDTO post(String appId, String appSecret, String url, Object param, List<Header> headers, Map<String, Object> pathMap) throws IOException {
        return post(appId, appSecret, url, param, "application/json;charset=UTF-8", headers, pathMap, (Map)null);
    }

    public static ResponseDTO post(String appId, String appSecret, String url, Object param, String contentType, List<Header> headers, Map<String, Object> pathMap, Map<String, File> fileMap) throws IOException {
        RequestDTO requestDTO = initRequest(param);
        List<Header> headerList = initHeader(appId, appSecret, contentType, headers);
        return sendPost(url, requestDTO, contentType, headerList, pathMap, fileMap);
    }


    private static RequestDTO initRequest(Object param) {
        RequestDTO requestDTO = new RequestDTO();
        if (Objects.nonNull(param)) {
            requestDTO.setParam(JSONObject.toJSONString(param));
        }
        return requestDTO;
    }

    private static List<Header> initHeader(String appId, String appSecret, String contentType, List<Header> headers) {
        List<Header> headerList = new ArrayList();
        long curr = System.currentTimeMillis();
        String signStr = appSecret + appId + curr + appSecret;
        String sign = DigestUtils.md5Hex(signStr.getBytes());
        int nonce = new Random ().nextInt(100);
        headerList.add(new BasicHeader("appId", appId));
        headerList.add(new BasicHeader("sign", sign));
        headerList.add(new BasicHeader("timeStamp", String.valueOf(curr)));
        headerList.add(new BasicHeader("nonce", String.valueOf(nonce)));
        if (Objects.isNull(contentType)) {
            headerList.add(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
        } else if (StringUtils.isNotEmpty(contentType) && !contentType.contains("multipart/form-data")) {
            headerList.add(new BasicHeader("Content-Type", contentType));
        }

        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(headers)) {
            headerList.addAll(headers);
        }

        return headerList;
    }

    private static ResponseDTO sendPost(String url, RequestDTO requestDTO, String contentType, List<Header> headers, Map<String, Object> pathMap, Map<String, File> fileMap) throws IOException {
        url = HttpUrlParser.buildUrl(url, pathMap);
        HttpPost request = initPostRequest(url, requestDTO, contentType, fileMap);
        return process(request, headers);
    }

    private static ResponseDTO process(HttpRequestBase request, List<Header> headers) throws IOException {
        HttpResponse httpResponse = processWithResponse(request, headers);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        ResponseDTO responseDTO = (ResponseDTO) JSON.parseObject(result, ResponseDTO.class);
        if (200 != statusCode) {
            logger.info("三方接口状态码异常,statusCode={}", statusCode);
            responseDTO.setCode(String.valueOf(statusCode));
            responseDTO.setMsg(result);
        }

        return responseDTO;
    }

    private static HttpResponse processWithResponse(HttpRequestBase request, List<Header> headers) throws IOException {
        if (headers != null) {
            Iterator var2 = headers.iterator();

            while(var2.hasNext()) {
                Header header = (Header)var2.next();
                request.addHeader(header);
            }
        }

        CloseableHttpClient httpClient = HttpClients.custom().build();
        return httpClient.execute(request);
    }

    private static HttpPost initPostRequest(String url, RequestDTO requestDTO, String contentType, Map<String, File> fileMap) {
        HttpPost httpPost;
        if (StringUtils.isNotEmpty(contentType) && contentType.contains("multipart/form-data")) {
            httpPost = initPostRequest(url, requestDTO, fileMap);
        } else {
            httpPost = initPostRequest(url, requestDTO);
        }

        return httpPost;
    }

    private static HttpPost initPostRequest(String url, RequestDTO requestDTO) {
        HttpPost request = new HttpPost(url);
        if (Objects.isNull(requestDTO)) {
            requestDTO = new RequestDTO();
        }

        StringEntity entity = new StringEntity(JSONObject.toJSONString(requestDTO), "UTF-8");
        entity.setContentEncoding("UTF-8");
        request.setEntity(entity);
        return request;
    }

    private static HttpPost initPostRequest(String url, RequestDTO requestDTO, Map<String, File> fileMap) {
        HttpPost request = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
        builder.setCharset(StandardCharsets.UTF_8);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (!CollectionUtils.isEmpty(fileMap)) {
            Iterator var7 = fileMap.keySet().iterator();

            while(var7.hasNext()) {
                String fileName = (String)var7.next();
                File file = (File)fileMap.get(fileName);
                String originName = StringUtils.isNotEmpty(file.getName()) ? file.getName() : fileName;
                builder.addBinaryBody(fileName, file, ContentType.MULTIPART_FORM_DATA, originName);
            }
        }

        ContentType contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), StandardCharsets.UTF_8);
        if (Objects.nonNull(requestDTO.getParam())) {
            JSONObject jsonObject = JSONObject.parseObject(requestDTO.getParam());
            Iterator var9 = jsonObject.keySet().iterator();

            while(var9.hasNext()) {
                String key = (String)var9.next();
                if (!StringUtils.isEmpty(key) && !Objects.isNull(jsonObject.get(key))) {
                    builder.addTextBody(key, jsonObject.get(key).toString(), contentType);
                }
            }
        }

        HttpEntity entity = builder.build();
        request.setEntity(entity);
        return request;
    }

}
