package com.study.fun.exception;


import com.study.fun.model.ResponseModel;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * <p>
 * 异常处理器
 * </p>
 *
 */
@RestControllerAdvice
public class GlobalException {
    private Logger log = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseModel IncorrectCredentialsException(IncorrectCredentialsException e) {
        log.error(e.getMessage(), e);
        ResponseModel model = new ResponseModel();
        model.setMessage("token过期");
        model.setResult(0);
        return model;
    }

    @ExceptionHandler(LockedAccountException.class)
    public ResponseModel LockedAccountException(LockedAccountException e) {
        log.error(e.getMessage(), e);
        ResponseModel model = new ResponseModel();
        model.setResult(0);
        model.setMessage("账号已被禁用,请联系管理员");
        return model;
    }

    @ExceptionHandler(Exception.class)
    public ResponseModel globalException(Throwable ex) {
        log.error("未知异常 ex: ", ex);
        ResponseModel model = new ResponseModel();
        model.setResult(0);
        model.setMessage("异常, 请联系管理员");
        return model;
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseModel handleAuthorizationException(AuthorizationException e) {
        log.error(e.getMessage(), e);
        ResponseModel model = new ResponseModel();
        model.setResult(0);
        model.setErrorcode("401");
        model.setMessage("没有权限");
        return model;
    }



}

