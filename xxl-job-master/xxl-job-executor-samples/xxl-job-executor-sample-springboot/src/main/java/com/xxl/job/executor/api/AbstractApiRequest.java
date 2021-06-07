package com.xxl.job.executor.api;


import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.api.token.ApiTokenRequest;
import com.xxl.job.executor.common.ApiParam;
import com.xxl.job.executor.domain.ResponseModel;
import com.xxl.job.executor.domain.sys.SysTaskDatetime;
import com.xxl.job.executor.service.sys.ISysTaskDatetimeService;
import com.xxl.job.executor.service.sys.ISysTaskTokenService;
import com.xxl.job.executor.utils.ApiRequestUrlUtil;
import com.xxl.job.executor.utils.DateUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractApiRequest {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ISysTaskTokenService iTskTokenService;
    @Resource
    private ApiTokenRequest tokenRequest;
    @Resource
    private ISysTaskDatetimeService taskDatetimeService;

    protected volatile String param;

    /**
     * 一次性插入数据的数量
     */
    protected final int limit = 50;

    @Retryable(value = {RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 1))
    public void excute() throws IOException {
        //1 获取token
        String token = iTskTokenService.getToken().getToken();
        if (token == null || "".equals(token)) {
            XxlJobLogger.log("token为空!");
            tokenRequest.excute();
        }

        //2 获取任务执行配置
        String apiName = initApiName();
        SysTaskDatetime tskDateTime = taskDatetimeService.getByApiName(apiName);
        if (tskDateTime == null) {
            XxlJobLogger.log("接口【" + apiName + "】未配置或不存在");
            return;
        }

        //执行时间
        String executetime = DateUtil.format(tskDateTime.getExecuteTime(), "yyyy-MM-dd'T'HH:mm:ss");
        //当前时间
        String currenttime = DateUtil.format(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
        //读取的首页数
        int pageIndex = tskDateTime.getPageIndex();
        //读取的下一页
        int nextPage = pageIndex + 1;
        // 是否有异常
        boolean isE = false;

        while (pageIndex <= nextPage) {
            ResponseEntity<? extends ResponseModel> responseEntity = null;
            Map<String, Object> map = getStringObjectMap(executetime, currenttime, pageIndex);
            Map<String, Object> requestExtMap = getRequestExtMap();
            if (null != requestExtMap) {
                map.putAll(requestExtMap);
            }

            String relativeApiUrl = initRelativeApiUrl();
            Class<? extends ResponseModel> resultMappingClass = initMappingClass();

            try {
                responseEntity = restTemplate.postForEntity(ApiParam.API_BASE_URL +
                                ApiRequestUrlUtil.getRequestUrl(relativeApiUrl, token)
                        , ApiRequestUrlUtil.getHttpEntity(map, apiName), resultMappingClass);

                if (null != responseEntity.getBody()) {
                    int result = responseEntity.getBody().getResult();
                    if (result == 1) {
                        if (responseEntity.getBody().getPagesize() > 0) {
                            insertOrUpdate(responseEntity.getBody());
                            pageIndex++;
                            if (responseEntity.getBody().getNextpage() == 0 || responseEntity.getBody().getNextpage() == null) {
                                nextPage = 0;
                                break;
                            } else {
                                nextPage = responseEntity.getBody().getNextpage();
                            }
                        } else {
                            pageIndex = 1;
                            break;
                        }

                    } else {
                        XxlJobLogger.log("任务名称:{}获取数据异常,异常信息为{}", apiName, responseEntity.getBody().getMsg());
                        break;
                    }
                } else {
                    pageIndex = 1;
                    break;
                }
            } catch (RestClientException e) {
                isE = true;
                XxlJobLogger.log("接口远程异常,{}", e.getMessage());
                String exceptionMessage = "调用任务接口【" + apiName + "】异常: " + e.getMessage() + e.getStackTrace().toString();
                throw new RemoteAccessException(exceptionMessage);
            } catch (Exception e) {
                nextPage = 0;
                isE = true;
                XxlJobLogger.log("接口异常,{}", e.getMessage());
                tskDateTime.setPageIndex(pageIndex);
                taskDatetimeService.updateById(tskDateTime);
            }
        }

        //3 执行完成后，将页码初始化，并保存当前时间
        if (!isE) {
            tskDateTime.setPageIndex(1);
            tskDateTime.setExecuteTime(new Date());
            taskDatetimeService.updateById(tskDateTime);
        }
    }

    @Recover
    public void recover(RetryException e) {
        XxlJobLogger.log("recovery,{}", e.getMessage());
    }

    public abstract String initApiName();


    public abstract String initRelativeApiUrl();


    public abstract Class<? extends ResponseModel> initMappingClass();

    public abstract void insertOrUpdate(ResponseModel entity);

    private Map<String, Object> getStringObjectMap(String executetime, String currenttime, int pageIndex) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("pageindex", pageIndex);
        map.put("starttime", executetime);
        map.put("endtime", currenttime);
        return map;
    }

    public abstract Map<String, Object> getRequestExtMap();

    public void setParam(String param) {
        this.param = param;
    }


}
