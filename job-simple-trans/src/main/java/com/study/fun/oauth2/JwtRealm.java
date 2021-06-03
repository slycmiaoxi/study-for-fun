package com.study.fun.oauth2;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.fun.constant.SecurityConstant;
import com.study.fun.domain.api.ApiUser;
import com.study.fun.domain.api.ApiUserToken;
import com.study.fun.service.api.IApiUserService;
import com.study.fun.service.api.IApiUserTokenService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 授权认证
 * </p>
 *
 */
@Component
public class JwtRealm extends AuthorizingRealm {
    @Autowired
    private IApiUserService apiUserService;
    @Autowired
    private IApiUserTokenService apiUserTokenService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    /**
     * <p>
     * 权限授权认证，主要用于role，permissins时认证
     * </p>
     *
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ApiUser user = (ApiUser) principals.getPrimaryPrincipal();
        String userId = user.getUserId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> permsSet = new HashSet<>();
        Set<String> roles = new HashSet<>();
        if (userId.equals(SecurityConstant.ADMIN_USER_ID)) {
            permsSet.add("delete");
            permsSet.add("update");
            permsSet.add("view");
            roles.add("admin");
        } else {
            permsSet.add("view");
            roles.add("test");
        }

        info.setStringPermissions(permsSet);
        info.setRoles(roles);
        return info;
    }

    /**
     * <p>
     * 登陆认证
     * </p>
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        if (jwtToken == null) {
            throw new AuthenticationException("token不能为空");
        }


        String accessToken = (String) token.getPrincipal();
        ApiUserToken userToken = apiUserTokenService.getOne(new QueryWrapper<ApiUserToken>().eq("access_token", accessToken));

        //判断token是否失效
        boolean flag = userToken == null || userToken.getTokenEndTime().getTime() < System.currentTimeMillis();

        if (flag) {
            throw new IncorrectCredentialsException("token失效");
        }
        ApiUser user = apiUserService.getById(userToken.getUserId());

        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被禁用,请联系管理员");
        }

        String salt = user.getSalt();
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, ByteSource.Util.bytes(salt), getName());
        return info;
    }
}
