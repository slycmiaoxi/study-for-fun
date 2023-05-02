package com.study.fun.server.model;



import com.study.fun.server.enums.SystemCodeEnum;

import java.util.UUID;

public class ResponseDTO {
    private String responseId;
    private String code;
    private String msg;
    private Object data;

    public static ResponseDTO build() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseId(UUID.randomUUID().toString());
        responseDTO.setCode(SystemCodeEnum.K_000000.getCode());
        responseDTO.setMsg(SystemCodeEnum.K_000000.getMsg());
        return responseDTO;
    }

    public static ResponseDTO build(Object data) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseId(UUID.randomUUID().toString());
        responseDTO.setCode(SystemCodeEnum.K_000000.getCode());
        responseDTO.setMsg(SystemCodeEnum.K_000000.getMsg());
        responseDTO.setData(data);
        return responseDTO;
    }

    public static ResponseDTO build(Object data, String responseId) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseId(responseId);
        responseDTO.setCode(SystemCodeEnum.K_000000.getCode());
        responseDTO.setMsg(SystemCodeEnum.K_000000.getMsg());
        responseDTO.setData(data);
        return responseDTO;
    }

    public static ResponseDTO build(String retCode, String retInfo) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResponseId(UUID.randomUUID().toString());
        responseDTO.setCode(retCode);
        responseDTO.setMsg(retInfo);
        return responseDTO;
    }

    public ResponseDTO() {
    }

    public String getResponseId() {
        return this.responseId;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return "ResponseDTO(responseId=" + this.getResponseId() + ", code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
