package com.study.fun.domain.api;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户token记录表
 * </p>
 *
 * @author 
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("api_user_token")
public class ApiUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private String userId;

    /**
     * 授权开始时间
     */
    private Date startTime;

    /**
     * 授权结束时间
     */
    private Date endTime;

    /**
     * 授权时间
     */
    private Date authTime;

    /**
     * Token令牌
     */
    private String accessToken;

    /**
     * Token生成时间
     */
    private Date tokenCreateTime;

    /**
     * Token失效时间
     */
    private Date tokenEndTime;

    /**
     * 当日token生成次数
     */
    private Integer tokenCount;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 最后更新人
     */
    private String lastUpdateUser;


}
