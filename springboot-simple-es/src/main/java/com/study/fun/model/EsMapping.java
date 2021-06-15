package com.study.fun.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EsMapping {

    /**
     * 名称
     */
    private String field;

    /**
     * 类型
     */
    private String type;

    /**
     * 分词器选择  0. not_analyzed   1. ik_smart 2. ik_max_word
     */
    private int participle;

    /**
     * 嵌套集合
     */
    private List<EsMapping> esMappingList;

    public EsMapping(String field, String type, int participle) {
        this.field = field;
        this.type = type;
        this.participle = participle;
    }
}
