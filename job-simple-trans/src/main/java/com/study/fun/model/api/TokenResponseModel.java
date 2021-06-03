package com.study.fun.model.api;

import com.study.fun.model.ResponseModel;
import lombok.Data;

/**
 * <p>
 * token 接受类
 * </p>
 *
 */
@Data
public class TokenResponseModel extends ResponseModel {

    protected Data data;

    @lombok.Data
    public class Data {
        /**
         * token
         */
        private String token;
        /**
         * 最新ip
         */
        private String lastip;
        /**
         * 最新时间
         */
        private String lasttime;
    }

}
