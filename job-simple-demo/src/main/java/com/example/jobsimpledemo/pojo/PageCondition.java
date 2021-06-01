package com.example.jobsimpledemo.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;


@Data
public class PageCondition {
    /**
     * 请求页码
     */
    @TableField(exist = false)
    private int page;

    /**
     * 每页数量
     */
    @TableField(exist = false)
    private int rows;

    /**
     * 总数
     */
    @TableField(exist = false)
    private int total;

    /**
     * 查询条件
     */
    @TableField(exist = false)
    private String searchName;

}
