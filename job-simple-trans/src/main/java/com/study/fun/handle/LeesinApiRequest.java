package com.study.fun.handle;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.fun.constant.SecurityConstant;
import com.study.fun.domain.api.ApiLeesin;
import com.study.fun.model.ResponseModel;
import com.study.fun.model.api.ApiLeesinResponseModel;
import com.study.fun.model.api.LeesinRequestModel;
import com.study.fun.service.api.IApiLeesinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 测试1推送接口处理
 * </p>
 *
 */
@Service
public class LeesinApiRequest extends AbstractApiRequest {
    @Resource
    private IApiLeesinService leesinService;

    @Override
    public ResponseModel action(String data) {
        LeesinRequestModel requestModel = JSON.parseObject(data, LeesinRequestModel.class);
        ApiLeesinResponseModel responseModel = new ApiLeesinResponseModel();
        PageHelper.startPage(requestModel.getPageindex(), SecurityConstant.DEFAULT_PAGE_SIZE);
        PageInfo<ApiLeesin> pageInfo = new PageInfo<>(leesinService.list());
        List<ApiLeesin> list;
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
