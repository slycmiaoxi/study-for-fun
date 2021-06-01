package com.example.jobsimpledemo.pojo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_job_detail")
public class SysJobDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Integer id;

    /**
     * 定时任务名称
     */
    private String jobName;

    /**
     * 定时任务组名
     */
    private String jobGroup;

    /**
     * 定时任务所在位置
     */
    private String jobClassName;

    /**
     * 定时任务执行表达式
     */
    private String jobCron;

    /**
     * 定时任务启动/停止   0停止  1启动
     */
    private Integer jobType;

    /**
     * 定时任务创建时间
     */
    private Date createTime;


}
