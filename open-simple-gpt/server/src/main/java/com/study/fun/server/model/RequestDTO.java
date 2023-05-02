package com.study.fun.server.model;

public class RequestDTO {
    private String param;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public RequestDTO() {

    }


    public String toString() {
        return "RequestDTO(param=" + this.getParam() + ")";
    }

}
