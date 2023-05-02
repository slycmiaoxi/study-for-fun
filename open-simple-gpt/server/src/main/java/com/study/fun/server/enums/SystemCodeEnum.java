package com.study.fun.server.enums;

public enum SystemCodeEnum {
    K_000000("000000", "success"),
    K_000001("000001", "系统异常"),
    K_300001("300001", "请求第三方异常"),
    K_300002("300002", "请求第三方业务异常"),
    K_400001("400001", "请求鉴权失败"),
    K_400002("400002", "无接口访问权限"),
    K_400003("400003", "请求参数不合法"),
    K_400004("400004", "无效url"),
    K_400005("400005", "请求过期");

    private final String code;
    private final String msg;

    private SystemCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
