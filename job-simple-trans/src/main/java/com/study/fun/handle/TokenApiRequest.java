package com.study.fun.handle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.fun.constant.SecurityConstant;
import com.study.fun.domain.api.ApiUser;
import com.study.fun.domain.api.ApiUserToken;
import com.study.fun.model.api.TokenResponseModel;
import com.study.fun.service.api.IApiUserService;
import com.study.fun.service.api.IApiUserTokenService;
import com.study.fun.utils.DateUtils;
import com.study.fun.utils.JwtTokenUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * token 请求
 * </p>
 *
 */
@Service
public class TokenApiRequest {
    @Autowired
    private IApiUserService userService;
    @Autowired
    private IApiUserTokenService userTokenService;

    public TokenResponseModel handle(String username, String password, HttpServletRequest request) {
        TokenResponseModel responseModel = new TokenResponseModel();
        responseModel.setResult(0);

        ApiUser user = userService.getOne(new QueryWrapper<ApiUser>().eq("user_name", username));
        if (user == null) {
            responseModel.setMessage("用户密码错误");
            return responseModel;
        }
        String pwd = new SimpleHash(SecurityConstant.ENCRYPTION_ALGORITHM, password, user.getSalt(), SecurityConstant.ENCRYPTION_COUNT).toString();
        if (!pwd.equals(user.getPassWord())) {
            responseModel.setMessage("用户密码错误");
            return responseModel;
        }

        ApiUserToken userToken = userTokenService.getById(user.getUserId());
        if (userToken == null) {
            responseModel.setMessage("未初始化用户token");
            return responseModel;
        }

        Date now = new Date();
        String token = JwtTokenUtils.generateValue();
        Date expireTime = DateUtils.getTodayEndTime();
        Integer apiCount = userToken.getTokenCount();
        userToken.setTokenCount(++apiCount);
        userToken.setAccessToken(token);
        userToken.setTokenEndTime(expireTime);
        userToken.setLastUpdateTime(now);
        userToken.setLastUpdateUser(username);

        userTokenService.updateById(userToken);

        responseModel.setResult(1);
        TokenResponseModel.Data data = responseModel.new Data();
        data.setLastip(request.getRemoteAddr());
        data.setToken(token);
        data.setLasttime(DateUtils.getDateTime());
        responseModel.setData(data);
        return responseModel;
    }
}
