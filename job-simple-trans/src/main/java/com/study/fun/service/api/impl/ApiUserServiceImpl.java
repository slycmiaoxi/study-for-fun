package com.study.fun.service.api.impl;

import com.study.fun.domain.api.ApiUser;
import com.study.fun.mapper.api.ApiUserMapper;
import com.study.fun.service.api.IApiUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-06-03
 */
@Service
public class ApiUserServiceImpl extends ServiceImpl<ApiUserMapper, ApiUser> implements IApiUserService {

}
