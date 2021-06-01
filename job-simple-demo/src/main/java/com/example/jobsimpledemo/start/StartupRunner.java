package com.example.jobsimpledemo.start;



import com.example.jobsimpledemo.job.CronScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@Order(value = 2)
public class StartupRunner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupRunner.class);

    @Autowired
    private CronScheduleJob cronScheduleJob;


    @Override
    public void run(String... args) throws Exception {
        LOGGER.info(">>>>>>>>>>接口定时任务启动！<<<<<<<<<<<");
        cronScheduleJob.scheduleJobsRun();

    }
}
