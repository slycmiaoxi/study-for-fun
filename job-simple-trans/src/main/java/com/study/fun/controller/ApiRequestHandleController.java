package com.study.fun.controller;


import com.alibaba.fastjson.JSONObject;
import com.study.fun.constant.ApiConst;
import com.study.fun.handle.LeesinApiRequest;
import com.study.fun.handle.TokenApiRequest;
import com.study.fun.handle.YurneroApiRequest;
import com.study.fun.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 数据推送统一接口
 * </p>
 *
 */
@RestController
@RequestMapping(value = ApiConst.API_PREFIX)
@RequiredArgsConstructor
public class ApiRequestHandleController {
    @Resource
    private TokenApiRequest tokenApiRequest;
    @Resource
    private LeesinApiRequest leesinApiRequest;
    @Resource
    private YurneroApiRequest yurneroApiRequest;


    /**
     * 获取接口token
     *
     * @param username 用户名
     * @param password 密码
     * @return ResponseModel
     */
    @RequestMapping("/gettoken")
    public ResponseModel gettoken(@RequestParam("username") String username,
                                  @RequestParam("password") String password, HttpServletRequest request) {
        return tokenApiRequest.handle(username, password, request);
    }

    /**
     * 推送测试1信息
     *
     * @param jsonObject 请求参数
     * @param token      请求token
     * @param request    HttpServletRequest对象
     * @return ResponseModel
     */
    @PostMapping("/getinformation/leesinlist")
    public ResponseModel leesinlist(@RequestBody JSONObject jsonObject, String token, HttpServletRequest request) {
        return leesinApiRequest.handle(jsonObject, token, request);
    }

    /**
     * 推送测试2信息
     *
     * @param jsonObject 请求参数
     * @param token      请求token
     * @param request    HttpServletRequest对象
     * @return ResponseModel
     */
    @PostMapping("/getinformation/yurnerolist")
    public ResponseModel yurnerolist(@RequestBody JSONObject jsonObject, String token, HttpServletRequest request) {
        return yurneroApiRequest.handle(jsonObject, token, request);
    }


}
