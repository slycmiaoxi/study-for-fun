package com.study.fun.portal.filter;



import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.study.fun.portal.config.IgnoreUrlsConfig;
import com.study.fun.portal.config.Result;
import com.study.fun.portal.config.UserContextHolder;
import com.study.fun.portal.config.UserInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 框架验证之外的扩展验证
 * 1.redis验证，token是否已经主动注销
 * 2.提取参数放入线程本地变量
 */
@Component
public class OauthFilter implements Filter {

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;

    private static final String verifyUrl = "http://localhost:8080/oauth/verify";

    //0:不走redis验证  1：redis验证
    @Value("${redis-token-flag}")
    private int redisTokenFlag;

    private static final long RENEW_DURATION = 3600 * 24;    //second

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        boolean isWhitelist = false;
        String[] ignoreUrls = ignoreUrlsConfig.getUrls().toArray(new String[]{});
        List<RequestMatcher> requestMatcherList = RequestMatchers.antMatchers(ignoreUrls);
        for (RequestMatcher matcher : requestMatcherList) {
            if (matcher.matches(httpRequest)) {
                isWhitelist = true;
                break;
            }
        }

        if (isWhitelist) {
            chain.doFilter(request, response);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //对存在token的数据，做用户信息提取
            if (authentication.isAuthenticated()) {
                //提取用户的身份信息
                Jwt jwt = (Jwt) authentication.getPrincipal();
                String userId = jwt.getClaim("userId");
                String userName = jwt.getClaim("userName");
                //redis验证用户缓存是否存在
                if (redisTokenFlag == 1) {
                    //远程sso调用
                    String tokenMd5 = "";
                    String authorization = getToken(httpRequest);
                    if (StringUtils.isNotEmpty(authorization)) {
                        String postResult = HttpRequest
                                .post(verifyUrl)
                                .header("Authorization", authorization)
                                .body(authorization)
                                .execute()
                                .body();
                        Result result = JSON.parseObject(postResult, Result.class);
                        Object json = result.getData();
                        tokenMd5 = (String) json;
                    }
                    if (!StringUtils.isEmpty(tokenMd5)) {
                        //token不同：说明该用户redis上已有最新的token，该账号已在其他地方登录，当前的token已失效
                        if (!tokenMd5.equals(DigestUtils.md5DigestAsHex(jwt.getTokenValue().getBytes()))) {
                            response.setContentType("application/json");
                            response.getWriter().println("{\"code\":96,\"message\":\"Token " +
                                    "Invalid\"," +
                                    "\"data\":\"\"}");
                            response.getWriter().flush();
                            return;
                        }
                    } else {
                        response.setContentType("application/json");
                        response.getWriter().println("{\"code\":99,\"message\":\"redis " +
                                "Authorization failed\"," +
                                "\"data\":\"\"}");
                        response.getWriter().flush();
                        return;
                    }
                }
                //检查续期，token接近有效期时自动续期
                if ((jwt.getExpiresAt().getEpochSecond() - System.currentTimeMillis() / 1000L) < RENEW_DURATION) {
                    response.setContentType("application/json");
                    response.getWriter().println("{\"code\":98,\"message\":\"Token Renew\"," +
                            "\"data\":\"\"}");
                    response.getWriter().flush();
                    return;
                }

                UserInfo user = new UserInfo();
                user.setUserId(userId);
                user.setUserName(userName);
                UserContextHolder.setUser(user);

            }
            try {
                chain.doFilter(request, response);
            } finally {
                UserContextHolder.removeUser();
            }
        }
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            authorization = request.getParameter("accessToken");
        }
        return authorization;
    }

    /**
     * 引入matcher
     */
    private static class RequestMatchers {

        public static List<RequestMatcher> antMatchers(HttpMethod httpMethod,
                                                       String... antPatterns) {
            String method = httpMethod == null ? null : httpMethod.toString();
            List<RequestMatcher> matchers = new ArrayList<>();
            for (String pattern : antPatterns) {
                matchers.add(new AntPathRequestMatcher(pattern, method));
            }
            return matchers;
        }


        public static List<RequestMatcher> antMatchers(String... antPatterns) {
            return antMatchers(null, antPatterns);
        }


        public static List<RequestMatcher> regexMatchers(HttpMethod httpMethod,
                                                         String... regexPatterns) {
            String method = httpMethod == null ? null : httpMethod.toString();
            List<RequestMatcher> matchers = new ArrayList<>();
            for (String pattern : regexPatterns) {
                matchers.add(new RegexRequestMatcher(pattern, method));
            }
            return matchers;
        }


        public static List<RequestMatcher> regexMatchers(String... regexPatterns) {
            return regexMatchers(null, regexPatterns);
        }

        private RequestMatchers() {
        }
    }
}