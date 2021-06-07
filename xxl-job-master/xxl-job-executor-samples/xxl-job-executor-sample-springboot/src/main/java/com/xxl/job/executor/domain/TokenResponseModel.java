package com.xxl.job.executor.domain;

import lombok.Data;


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
