package com.xxl.job.executor.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxl.job.executor.domain.sys.SysTaskDatetime;


public interface ISysTaskDatetimeService extends IService<SysTaskDatetime> {


    SysTaskDatetime getByApiName(String apiName);

}
