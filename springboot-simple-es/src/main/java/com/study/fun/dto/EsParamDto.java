package com.study.fun.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class EsParamDto implements Serializable {
    /**
     * 数据总条数
     */
    private Long total;


    /**
     * 当前页
     */
    private int page = 1;

    /**
     * 每页大小
     */
    private int size = 1;

    /**
     * 是否分页
     */
    private Boolean isPage = true;

    /**
     * 区间查询参数
     */
    private Map<String, RangConditionDTO> rangConditionMap ;

    /**
     * 时间区间查询参数
     */
    private Map<String, RangConditionsToTimeModelDTO> rangConditionsToTimeModelMap ;

    /**
     * 模糊相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> likeSearchCondition ;

    /**
     * or条件查询
     */
    private Map<String, Object> orSearchCondition ;

    /**
     * or条件查询
     */
    private Map<String, Object> orLikeSearchCondition ;

    /**
     * or条件查询集合类操作
     */
    private List<Map<String, Object>> orSearchConditionList ;

    /**
     * 相等查询条件，多个查询条件","进行切割
     */
    private Map<String, Object> equalsSearchCondition ;

    /**
     * in 查询
     */
    private Map<String, List> inSearchCondition ;

    /**
     * 模糊不相等的条件，多个查询条件","进行切割
     */
    private Map<String, Object> noLikeSearchConditioin ;

    /**
     * 不相等的条件，多个查询条件","进行切割
     */
    private Map<String, Object> noEqualsSearchConditioin ;

    /**
     * 为空过滤
     */
    private List<String> isNullConditioin ;

    /**
     * 不为空过滤
     */
    private List<String> isNotNullConditioin ;

    /**
     * 排序字段，关键字asc，desc
     */
    private Map<String, String> sortFileds ;

    /**
     * 排序字段集合，方便对排序顺序的控制 关键字asc，desc
     */
    private List<Map<String, String>> sortFiledsList ;

    /**
     * 高亮字段
     */
    private List<String> hightFieldList ;

    /**
     * 去重字段
     */
    private String collapseField;

    /**
     * 指定查询结果包含的字段
     */
    private String[] fetchSourceIncludes;

    /**
     * 指定查询结果不包含的字段
     */
    private String[] fetchSourceExcludes;

    /**
     * 分词字段
     */
    private Map<String, Object> analyzersField ;

    public String getSortFileds(String key) {
        return sortFileds.get(key);
    }

    public RangConditionDTO getRangConditionMap(String key) {
        return rangConditionMap.get(key);
    }

    public RangConditionsToTimeModelDTO getRangConditionsToTimeModelMap(String key) {
        return rangConditionsToTimeModelMap.get(key);
    }

}
