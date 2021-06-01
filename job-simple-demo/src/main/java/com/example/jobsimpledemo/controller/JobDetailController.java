package com.example.jobsimpledemo.controller;


import com.example.jobsimpledemo.pojo.JobDetailVo;
import com.example.jobsimpledemo.pojo.Pageable;
import com.example.jobsimpledemo.pojo.domain.SysJobDetail;
import com.example.jobsimpledemo.service.ISysJobDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/jobDetail")
public class JobDetailController {

    @Autowired
    private ISysJobDetailService jobService;

    @RequestMapping("/list")
    @ResponseBody
    public Pageable list(JobDetailVo jobDetail) {
        return jobService.listPageInfo(jobDetail);
    }

    @PostMapping("/update")
    @ResponseBody
    public void updateJobType(@RequestParam Integer id, @RequestParam int jobType) {
        SysJobDetail jobDetail = jobService.getById(id);
        if (jobType == 0) {
            jobService.pauseJob(jobDetail.getJobClassName(), jobDetail.getJobGroup());
        } else {
            jobService.resumeJob(jobDetail.getJobClassName(), jobDetail.getJobGroup());
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam Integer id) {
        SysJobDetail jobDetail = jobService.getById(id);
        jobService.deleteJob(jobDetail.getJobClassName(), jobDetail.getJobGroup());
    }

}

