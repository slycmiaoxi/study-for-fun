package com.study.fun.portal.config;

/**
 * 存放用户的身份信息
 */
public class UserContextHolder {

    private final static ThreadLocal<UserInfo> userLocal = new ThreadLocal();

    public static UserInfo getUser(){
        return userLocal.get();
    }

    public static void setUser(UserInfo user){
        userLocal.set(user);
    }

    public static void removeUser(){
        userLocal.remove();
    }
}
