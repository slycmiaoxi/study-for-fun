package com.example.jobsimpledemo.job;

import com.example.jobsimpledemo.pojo.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Recover;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
public class bpArticleJob extends BaseJob {
    @Resource
    private RestTemplate restTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("add your code");
    }

    @Recover
    public void recover(RetryException e) {
        log.info("recovery,{}", e.getMessage());
    }

}
