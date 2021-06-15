package com.study.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RangConditionDTO implements Serializable {

    /**
     * 开始区间
     */
    private String beginValue;

    /**
     * 结束区间
     */
    private String endValue;
}

