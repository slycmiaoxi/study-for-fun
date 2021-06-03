package com.study.fun.handle;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.fun.domain.api.ApiAuth;
import com.study.fun.domain.api.ApiUserAuth;
import com.study.fun.domain.api.ApiUserToken;
import com.study.fun.model.ResponseModel;
import com.study.fun.service.api.IApiAuthService;
import com.study.fun.service.api.IApiUserAuthService;
import com.study.fun.service.api.IApiUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 统一请求处理 请求模板
 * </p>
 *
 */
public abstract class AbstractApiRequest {
    @Autowired
    private IApiUserTokenService apiUserTokenService;
    @Autowired
    private IApiAuthService apiAuthService;
    @Autowired
    private IApiUserAuthService apiUserauthService;

    public ResponseModel handle(JSONObject jsonObject, String token, HttpServletRequest request) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResult(0);
        String servletPath = request.getServletPath();
        ApiAuth apiService = apiAuthService.getOne(new QueryWrapper<ApiAuth>().eq("service_url", servletPath));
        if (null == apiService) {
            responseModel.setMessage("该用户无访问权限");
            return responseModel;
        }
        ApiUserToken userToken = apiUserTokenService.getOne(new QueryWrapper<ApiUserToken>().eq("access_token", token));
        if (userToken == null) {
            responseModel.setMessage("该用户无访问权限");
            return responseModel;
        }

            int count = apiUserauthService.count(new QueryWrapper<ApiUserAuth>().eq("user_id", userToken.getUserId())
                .eq("service_id", apiService.getServiceId()));
        if (count == 0) {
            responseModel.setMessage("该用户无访问权限");
            return responseModel;
        }

        String data = jsonObject.getString("data");
        if (null == data) {
            responseModel.setMessage("参数错误");
            return responseModel;
        }

        return action(data);
    }

    /**
     * 业务处理
     *
     * @param data 请求参数json格式
     * @return ResponseModel
     */
    public abstract ResponseModel action(String data);


}
