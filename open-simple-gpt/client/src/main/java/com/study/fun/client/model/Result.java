package com.study.fun.client.model;

import java.io.Serializable;
import java.util.Objects;

public class Result<T> implements Serializable {
    private int code;
    private String message = "";
    private T data;

    private static final int SUCCESS_CODE = 0;

    public static <T> Result<T> success(T data) {
        return handlerWrapperResponse(0, "ok", data);
    }

    public static <T> Result<T> error(int code, String message) {
        return (Result<T>) handlerWrapperResponse(code, message, (Object) null);
    }

    public static <T> Result<T> error(int code, String message, T data) {
        return (Result<T>) handlerWrapperResponse(code, message, data);
    }

    public static Result fail(int retCode, String retInfo, Object data) {
        Result result = new Result();
        result.setCode(retCode);
        result.setMessage(retInfo);
        result.setData(data);
        return result;
    }

    public boolean isError() {
        return !Objects.equals(this.getCode(), SUCCESS_CODE);
    }

    private static <T> Result<T> handlerWrapperResponse(int code, String message, T data) {
        Result<T> result = new Result();
        result.setCode(code);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public Result() {
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else {
            Result<?> other = (Result) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getCode() != other.getCode()) {
                return false;
            } else {
                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Result;
    }

    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        result = result * 59 + this.getCode();
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "ElsResponse(code=" + this.getCode() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

}
