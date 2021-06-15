package com.study.fun.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RangConditionsToTimeModelDTO implements Serializable {

    /**
     * 开始时间
     */
    private Timestamp beginTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;
}

