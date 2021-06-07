package com.xxl.job.executor.domain;

import lombok.Data;

import java.util.List;

@Data
public class YurneroRequestModel extends ResponseModel {

    protected List<Data> data;

    @lombok.Data
    public static class Data {
        private Integer id;

        private String apiTest3;

        private String apiTest4;
    }
}
