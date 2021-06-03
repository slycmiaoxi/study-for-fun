package com.study.fun.oauth2;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>
 * JWT token
 * </p>
 *
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
