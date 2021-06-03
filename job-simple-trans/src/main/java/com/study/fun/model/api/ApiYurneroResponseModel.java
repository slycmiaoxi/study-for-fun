package com.study.fun.model.api;

import com.study.fun.domain.api.ApiYurnero;
import com.study.fun.model.ResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class ApiYurneroResponseModel extends ResponseModel {

    protected List<ApiYurnero> data;
}
