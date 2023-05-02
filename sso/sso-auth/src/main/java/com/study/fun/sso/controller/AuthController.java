package com.study.fun.sso.controller;

import com.alibaba.druid.util.StringUtils;
import com.study.fun.sso.domain.Oauth2TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.DigestUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Oauth2获取令牌接口
 */
@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    private static final String PREFIX_TOKEN = "token_alive_";

    /**
     * Oauth2登录认证
     */
    @PostMapping(value = "/token")
    public Map postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken;
        Map<String, Object> resultMap = new HashMap<>();
        try {
            oAuth2AccessToken = tokenEndpoint.postAccessToken(principal,
                    parameters).getBody();
        } catch (Exception e) {
            log.error("登录失败", e);
            resultMap.put("code", 99);
            resultMap.put("msg", e.getMessage());
            resultMap.put("data", "");
            return resultMap;
        }
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead("Bearer ").build();

        //redis保活
        String userId = oAuth2AccessToken.getAdditionalInformation().get("userId").toString();
        int expiresIn = oAuth2AccessToken.getExpiresIn();
        //一个人只保留一个最新的token
        //stringRedisTemplate.opsForValue().set(PREFIX + userId, userId, expiresIn, TimeUnit.SECONDS);
        String md5 = DigestUtils.md5DigestAsHex(oAuth2AccessToken.getValue().getBytes());
        stringRedisTemplate.opsForValue().set(PREFIX_TOKEN + userId, md5, expiresIn, TimeUnit.SECONDS);

        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("data", oauth2TokenDto);
        return resultMap;
    }

    /**
     * 用户登出
     *
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public Object logout(@RequestHeader("Authorization") String token) {

        if (!StringUtils.isEmpty(token) && token.length() > 7) {
            Method method;
            try {
                method = jwtAccessTokenConverter.getClass().getDeclaredMethod("decode",
                        String.class);
                method.setAccessible(true);
                Map<String, Object> claims =
                        (Map<String, Object>) method.invoke(jwtAccessTokenConverter,
                                token.substring(7));
                if (claims != null && claims.get("userId") != null) {
                    stringRedisTemplate.delete(PREFIX_TOKEN + claims.get("userId"));
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("ReflectiveOperationException", e);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "success");
        result.put("data", "");
        return result;
    }

    @PostMapping("/verify")
    public Map<String, Object> postTokenMd5(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        String tokenMd5 = "";
        if (!StringUtils.isEmpty(token) && token.length() > 7) {
            Method method;
            try {
                method = jwtAccessTokenConverter.getClass().getDeclaredMethod("decode",
                        String.class);
                method.setAccessible(true);
                Map<String, Object> claims =
                        (Map<String, Object>) method.invoke(jwtAccessTokenConverter,
                                token.substring(7));
                if (claims != null && claims.get("userId") != null) {
                    tokenMd5 = stringRedisTemplate.opsForValue().get(PREFIX_TOKEN + claims.get("userId"));
                }
            } catch (NoSuchMethodException e) {
                log.error("ReflectiveOperationException", e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        result.put("code", 0);
        result.put("message", "success");
        result.put("data", tokenMd5);
        return result;
    }
}
