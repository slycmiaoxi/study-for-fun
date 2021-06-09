package com.study.fun.domain.api;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("api_leesin")
public class ApiLeesin implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String apiTest1;

    private String apiTest2;


}
