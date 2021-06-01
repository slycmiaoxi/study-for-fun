package com.example.jobsimpledemo.job;



import com.example.jobsimpledemo.pojo.domain.SysJobDetail;
import com.example.jobsimpledemo.service.ISysJobDetailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class CronScheduleJob {
    @Autowired
    private ISysJobDetailService jobService;

    public void scheduleJobsRun() throws SchedulerException {
        List<SysJobDetail> dwTdJobDetails = jobService.findInitJob();
        dwTdJobDetails.forEach(dwTdJobDetail -> {
            try {
                jobService.addJob(dwTdJobDetail, false);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("初始化定时失败！{}", e.getMessage());
            }
        });
    }

}
