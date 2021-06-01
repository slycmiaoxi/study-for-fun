package com.example.jobsimpledemo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.jobsimpledemo.pojo.JobDetailVo;
import com.example.jobsimpledemo.pojo.domain.SysJobDetail;
import org.apache.ibatis.annotations.Param;


import java.util.List;


public interface SysJobDetailMapper extends BaseMapper<SysJobDetail> {

    List<SysJobDetail> listPageInfo(@Param("data") JobDetailVo jobDetail);
}
