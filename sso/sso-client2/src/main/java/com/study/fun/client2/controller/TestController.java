package com.study.fun.client2.controller;

import com.study.fun.portal.config.UserContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("test")
    public Object test(){
        return UserContextHolder.getUser().getUserId();
    }
}
