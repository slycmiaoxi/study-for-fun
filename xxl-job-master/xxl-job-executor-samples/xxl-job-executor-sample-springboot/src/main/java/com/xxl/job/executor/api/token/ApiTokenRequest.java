package com.xxl.job.executor.api.token;

import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.common.ApiParam;
import com.xxl.job.executor.domain.TokenResponseModel;
import com.xxl.job.executor.domain.sys.SysTaskDatetime;
import com.xxl.job.executor.enumerate.TaskEnumUtils;
import com.xxl.job.executor.service.sys.ISysTaskDatetimeService;
import com.xxl.job.executor.service.sys.ISysTaskTokenService;
import com.xxl.job.executor.utils.JoinerUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;


@Service
public class ApiTokenRequest {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ISysTaskTokenService taskTokenService;
    @Resource
    private ISysTaskDatetimeService taskDatetimeService;

    @Retryable(value = {RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 1))
    public void excute() throws IOException {
        try {
            //1 拼接get请求
            String params = JoinerUtil.joinAnd(JoinerUtil.joinEquals("username", ApiParam.API_USER_NAME), JoinerUtil.joinEquals("password", ApiParam.API_USER_PASSWORD));
            String tokenUrl = JoinerUtil.joinQuestion(ApiParam.API_BASE_URL + "gettoken", params);

            ResponseEntity<TokenResponseModel> responseEntity =
                    restTemplate.getForEntity(tokenUrl, TokenResponseModel.class);
            TokenResponseModel entity = responseEntity.getBody();
            //2 保存token
            if (entity != null) {
                String token = entity.getData().getToken();
                taskTokenService.insertOrUpdate(token);
                XxlJobLogger.log("今日token为【{}】", token);
            }

            //3 执行完成后，将页码初始化，并保存当前时间
            SysTaskDatetime tskDateTime = taskDatetimeService.getByApiName(TaskEnumUtils.taskDatetimeType.TOKEN.key);
            if (null != tskDateTime) {
                tskDateTime.setPageIndex(1);
                tskDateTime.setExecuteTime(new Date());
                taskDatetimeService.updateById(tskDateTime);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            String errorMsg = "获取【token】失败{}" + e.getMessage();
            throw new RemoteAccessException(errorMsg);
        }
    }
}
