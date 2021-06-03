package com.study.fun.handle;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.fun.constant.SecurityConstant;
import com.study.fun.domain.api.ApiYurnero;
import com.study.fun.model.ResponseModel;
import com.study.fun.model.api.ApiYurneroResponseModel;
import com.study.fun.model.api.YurneroRequestModel;
import com.study.fun.service.api.IApiYurneroService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class YurneroApiRequest extends AbstractApiRequest {

    @Resource
    private IApiYurneroService yurneroService;

    @Override
    public ResponseModel action(String data) {
        YurneroRequestModel requestModel = JSON.parseObject(data, YurneroRequestModel.class);
        ApiYurneroResponseModel responseModel = new ApiYurneroResponseModel();
        PageHelper.startPage(requestModel.getPageindex(), SecurityConstant.DEFAULT_PAGE_SIZE);
        PageInfo<ApiYurnero> pageInfo = new PageInfo<>(yurneroService.list());
        List<ApiYurnero> list;
        if (pageInfo.isHasNextPage() || pageInfo.getPages() == requestModel.getPageindex()) {
            list = pageInfo.getList();
        } else {
            list = new ArrayList<>();
        }
        responseModel.setData(list);
        responseModel.setResult(1);
        responseModel.setPageindex(requestModel.getPageindex());
        responseModel.setPagesize(pageInfo.getPageSize());
        responseModel.setNextpage(pageInfo.getNextPage());
        return responseModel;
    }
}
