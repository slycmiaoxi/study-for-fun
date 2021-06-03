package com.study.fun.model.api;

import com.study.fun.domain.api.ApiLeesin;
import com.study.fun.model.ResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class ApiLeesinResponseModel extends ResponseModel {

    protected List<ApiLeesin> data;
}
