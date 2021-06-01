package com.example.jobsimpledemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.jobsimpledemo.exception.JobExistsException;
import com.example.jobsimpledemo.mapper.SysJobDetailMapper;
import com.example.jobsimpledemo.pojo.JobDetailVo;
import com.example.jobsimpledemo.pojo.Pageable;
import com.example.jobsimpledemo.pojo.domain.SysJobDetail;
import com.example.jobsimpledemo.service.ISysJobDetailService;
import com.example.jobsimpledemo.util.ClassUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;


@Service
@Slf4j
public class SysJobDetailServiceImpl extends ServiceImpl<SysJobDetailMapper, SysJobDetail> implements ISysJobDetailService {
    @Resource
    private Scheduler scheduler;
    @Resource
    private SysJobDetailMapper jobDetailMapper;

    @Override
    public List<SysJobDetail> findInitJob() {
        QueryWrapper<SysJobDetail> jobDetailQueryWrapper = new QueryWrapper<>();
        jobDetailQueryWrapper.eq("job_type", 1);
        List<SysJobDetail> jobDetails = jobDetailMapper.selectList(jobDetailQueryWrapper);
        return jobDetails;
    }

    @Override
    public void addJob(SysJobDetail dwTdJobDetailModel, boolean isCheck) throws SchedulerException, ClassNotFoundException {
        scheduler.start();
        System.out.println(dwTdJobDetailModel.toString());
        JobDetail jobDetail = JobBuilder
                .newJob(ClassUtil.getJobClass(dwTdJobDetailModel.getJobClassName()))
                .withIdentity(dwTdJobDetailModel.getJobClassName(), dwTdJobDetailModel.getJobGroup())
                .build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule(dwTdJobDetailModel.getJobCron());
        CronTrigger cronTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity(dwTdJobDetailModel.getJobClassName(), dwTdJobDetailModel.getJobGroup())
                .withSchedule(scheduleBuilder)
                .build();
        if (isCheck) {
            if (scheduler.getJobGroupNames().contains(dwTdJobDetailModel.getJobGroup()) ||
                    !jobDetailHandle(dwTdJobDetailModel.getJobClassName(),
                            dwTdJobDetailModel.getJobGroup(), "addJob")) {
                jobExistsException();
            }
        }
        scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("定时任务" + dwTdJobDetailModel.getJobName() + "开始");
    }

    @Override
    public Pageable listPageInfo(JobDetailVo jobDetail) {
        Page<JobDetailVo> page = PageHelper.startPage(jobDetail.getPage(), jobDetail.getRows()).doSelectPage(
                () -> {
                    jobDetailMapper.listPageInfo(jobDetail);
                }
        );
        return Pageable.builder().buildForJqGrid(page);
    }

    @Override
    public void pauseJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                jobDetailHandle(jobName,
                        jobGroup, "stopJob");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJob(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                jobDetailHandle(jobName,
                        jobGroup, "deleteJob");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroup) {
        try {

            JobKey jobKey = new JobKey(jobName, jobGroup);
            if (scheduler.checkExists(jobKey)) {
                scheduler.triggerJob(jobKey);
                jobDetailHandle(jobName,
                        jobGroup, "recoverJob");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务已存在异常方法
     */
    private void jobExistsException() {
        throw new JobExistsException("任务已存在");
    }

    /**
     * jobDetail操作
     *
     * @param jobClassName
     * @param jobGroupName
     * @param branch       addJob 添加任务 stopJob 停止任务 recoverJob 恢复任务 pauseAll 停止全部任务
     *                     recoverAll 恢复全部任务
     */
    private boolean jobDetailHandle(String jobClassName, String jobGroupName, String branch, String... cron) {
        SysJobDetail dwTdJobDetail = null;
        QueryWrapper<SysJobDetail> jobDetailQueryWrapper = new QueryWrapper<>();
        switch (branch) {
            case "addJob":
                jobDetailQueryWrapper.eq("job_class_name", jobClassName).eq("job_group", jobGroupName);
                List<SysJobDetail> dwTdJobDetails = jobDetailMapper
                        .selectList(jobDetailQueryWrapper);
                return dwTdJobDetails == null || dwTdJobDetails.size() == 0 ? true : false;
            case "stopJob":
                jobDetailQueryWrapper = new QueryWrapper<>();
                return jobDetailUpdate(dwTdJobDetail, jobDetailQueryWrapper, jobClassName, jobGroupName, 0);
            case "recoverJob":
                jobDetailQueryWrapper = new QueryWrapper<>();
                return jobDetailUpdate(dwTdJobDetail, jobDetailQueryWrapper, jobClassName, jobGroupName, 1);
            case "pauseAll":
                jobDetailQueryWrapper = new QueryWrapper<>();
                return jobDetailUpdate(dwTdJobDetail, jobDetailQueryWrapper, null, null, 0);
            case "recoverAll":
                jobDetailQueryWrapper = new QueryWrapper<>();
                return jobDetailUpdate(dwTdJobDetail, jobDetailQueryWrapper, null, null, 1);
            case "deleteJob":
                jobDetailQueryWrapper = new QueryWrapper<>();
                return jobDetailUpdate(dwTdJobDetail, jobDetailQueryWrapper, jobClassName, jobGroupName, -1);
            case "modifyJobTime":
                jobDetailQueryWrapper = new QueryWrapper<>();
                return jobDetailUpdate(dwTdJobDetail, jobDetailQueryWrapper, jobClassName, jobGroupName, 1, cron);
            default:
                return true;
        }
    }

    /**
     * 数据操作
     *
     * @param dwTdJobDetail
     * @param jobDetailQueryWrapper
     * @param jobClassName
     * @param jobGroupName
     * @param jobType
     * @return
     */
    private boolean jobDetailUpdate(SysJobDetail dwTdJobDetail, QueryWrapper<SysJobDetail> jobDetailQueryWrapper
            , String jobClassName, String jobGroupName, int jobType, String... cron) {
        dwTdJobDetail = new SysJobDetail();
        if (cron != null && cron.length > 0) {
            dwTdJobDetail.setJobCron(String.valueOf(cron[0]));
        }
        dwTdJobDetail.setJobType(jobType);
        if (jobClassName != null && jobGroupName != null) {
            jobDetailQueryWrapper.eq("job_class_name", jobClassName).eq("job_group", jobGroupName);
        }
        if (jobType == -1) {
            return jobDetailMapper.delete(jobDetailQueryWrapper) > 0 ? true : false;
        }
        return jobDetailMapper.update(dwTdJobDetail, jobDetailQueryWrapper) > 0 ? true : false;
    }
}
