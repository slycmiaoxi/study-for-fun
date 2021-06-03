package com.study.fun.domain.api;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接口服务表
 * </p>
 *
 * @author 
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("api_auth")
public class ApiAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务ID
     */
    @TableId
    private String serviceId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务说明
     */
    private String serviceRemark;

    /**
     * 服务相对路径
     */
    private String serviceUrl;

    /**
     * 服务状态
     */
    private Integer status;


}
