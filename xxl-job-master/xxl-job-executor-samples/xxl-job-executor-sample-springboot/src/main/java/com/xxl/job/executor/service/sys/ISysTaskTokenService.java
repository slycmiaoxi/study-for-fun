package com.xxl.job.executor.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxl.job.executor.domain.sys.SysTaskToken;


public interface ISysTaskTokenService extends IService<SysTaskToken> {


    void insertOrUpdate(String token);


    SysTaskToken getToken();
}
