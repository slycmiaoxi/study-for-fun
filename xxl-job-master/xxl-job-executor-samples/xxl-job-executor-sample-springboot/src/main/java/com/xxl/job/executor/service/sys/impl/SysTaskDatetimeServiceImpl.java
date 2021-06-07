package com.xxl.job.executor.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxl.job.executor.domain.sys.SysTaskDatetime;
import com.xxl.job.executor.mapper.sys.SysTaskDatetimeMapper;
import com.xxl.job.executor.service.sys.ISysTaskDatetimeService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class SysTaskDatetimeServiceImpl extends ServiceImpl<SysTaskDatetimeMapper, SysTaskDatetime> implements ISysTaskDatetimeService {
    @Resource
    private SysTaskDatetimeMapper taskDatetimeMapper;

    @Override
    public SysTaskDatetime getByApiName(String apiName) {
        List<SysTaskDatetime> taskDatetimeList = taskDatetimeMapper.selectList(new QueryWrapper<SysTaskDatetime>().eq("api_name", apiName));
        return CollectionUtils.isEmpty(taskDatetimeList) ? null : taskDatetimeList.get(0);
    }
}
