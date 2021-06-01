package com.example.jobsimpledemo.sample.timertask;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeTaskSample {


    public static void main(String[] args) {
        //1 创建定时任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("task :"+new Date());
            }
        };

        //2 创建定时器
        Timer timer = new Timer();

        //3 开始执行(每隔3s打印当前时间)
        timer.schedule(task,10,300000);

    }

}
