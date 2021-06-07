package com.xxl.job.executor.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxl.job.executor.domain.sys.SysTaskToken;
import com.xxl.job.executor.mapper.sys.SysTaskTokenMapper;
import com.xxl.job.executor.service.sys.ISysTaskTokenService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class SysTaskTokenServiceImpl extends ServiceImpl<SysTaskTokenMapper, SysTaskToken> implements ISysTaskTokenService {
    @Resource
    private SysTaskTokenMapper taskTokenMapper;

    @Override
    public void insertOrUpdate(String token) {
        SysTaskToken taskToken = getToken();
        Date now = new Date();
        if (null == taskToken) {
            taskToken = new SysTaskToken();
            taskToken.setToken(token);
            taskToken.setDateTime(now);
            taskToken.setId(1);
            taskTokenMapper.insert(taskToken);
        } else {
            taskToken.setToken(token);
            taskToken.setDateTime(now);
            taskTokenMapper.updateById(taskToken);
        }
    }

    @Override
    public SysTaskToken getToken() {
        List<SysTaskToken> tokenList = taskTokenMapper.selectList(new QueryWrapper<SysTaskToken>());
        return CollectionUtils.isEmpty(tokenList) ? null : tokenList.get(0);
    }
}
