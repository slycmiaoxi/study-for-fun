package com.example.jobsimpledemo.pojo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2020-03-17
 */
@Data
public class JobDetailVo extends PageCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String jobName;

    private String jobGroup;

    private String jobClassName;

    private String jobCron;

    private Integer jobType;

    private Date createTime;


}
