package com.example.jobsimpledemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.jobsimpledemo.pojo.JobDetailVo;
import com.example.jobsimpledemo.pojo.Pageable;
import com.example.jobsimpledemo.pojo.domain.SysJobDetail;
import org.quartz.SchedulerException;


import java.util.List;


public interface ISysJobDetailService extends IService<SysJobDetail> {

    List<SysJobDetail> findInitJob();

    void addJob(SysJobDetail jobDetail, boolean isCheck) throws SchedulerException, ClassNotFoundException;

    Pageable listPageInfo(JobDetailVo jobDetail);

    void pauseJob(String jobName, String jobGroup);

    void deleteJob(String jobName, String jobGroup);

    void resumeJob(String jobName, String jobGroup);
}
