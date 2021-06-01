package com.example.jobsimpledemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
//@Async
@MapperScan({"com.example.jobsimpledemo.mapper"})
public class JobSimpleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSimpleDemoApplication.class, args);
    }

}
