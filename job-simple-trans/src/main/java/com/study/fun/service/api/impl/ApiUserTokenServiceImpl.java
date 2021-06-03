package com.study.fun.service.api.impl;

import com.study.fun.domain.api.ApiUserToken;
import com.study.fun.mapper.api.ApiUserTokenMapper;
import com.study.fun.service.api.IApiUserTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户token记录表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-06-03
 */
@Service
public class ApiUserTokenServiceImpl extends ServiceImpl<ApiUserTokenMapper, ApiUserToken> implements IApiUserTokenService {

}
