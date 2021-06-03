package com.study.fun.service.api.impl;

import com.study.fun.domain.api.ApiUserAuth;
import com.study.fun.mapper.api.ApiUserAuthMapper;
import com.study.fun.service.api.IApiUserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * api_user_auth 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-06-03
 */
@Service
public class ApiUserAuthServiceImpl extends ServiceImpl<ApiUserAuthMapper, ApiUserAuth> implements IApiUserAuthService {

}
