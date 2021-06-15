package com.study.fun.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.study.fun.constant.EsConstant;
import com.study.fun.dto.EsParamDto;
import com.study.fun.dto.RangConditionDTO;
import com.study.fun.dto.RangConditionsToTimeModelDTO;
import com.study.fun.model.EsMapping;
import com.study.fun.model.PageResult;
import com.study.fun.utils.EsMappingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class EsModel {

    private static RestHighLevelClient client;

    /**
     * 服务器地址
     */
    private static String host = "";
    /**
     * 端口
     */
    private static int port = 9200;

    /**
     * 用户名
     */
    private static String user = "";

    /**
     * 密码
     */
    private static String password = "";

    public EsModel() {
        try {
            RestClientBuilder clientBuilder = RestClient
                    .builder(new HttpHost(host, port, "http"));
            if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));
                clientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpAsyncClientBuilder.setKeepAliveStrategy((resp, context) -> 30);
                    return httpAsyncClientBuilder;
                });
            } else {
                clientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
                    httpAsyncClientBuilder.setKeepAliveStrategy((resp, context) -> 30);
                    return httpAsyncClientBuilder;
                });
            }
            client = new RestHighLevelClient(clientBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存数据
     *
     * @param index
     * @param id
     * @param paramJson
     * @param clazz
     * @return
     */
    public boolean add(String index, String id, String paramJson, Class clazz) {
        boolean result = isIndexExists(index);

        if (!result) {
            boolean createResult = createIndexAndCreateMapping(index, EsMappingUtils.getFieldInfo(clazz));
            if (!createResult) {
                log.info("索引[{}],主键[{}]创建失败", index, id);
                return false;
            }
        }

        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        indexRequest.source(paramJson, XContentType.JSON);

        IndexResponse response = null;
        try {
            response = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("索引[{}],主键[{}]保存异常:{}", index, id, e);
            return false;
        }

        if (IndexResponse.Result.CREATED.equals(response.getResult())) {
            log.info("索引[{}],主键[{}]保存成功", index, id);
            return true;
        } else if (IndexResponse.Result.UPDATED.equals(response.getResult())) {
            log.info("索引[{}],主键[{}]修改成功", index, id);
            return true;
        }
        return false;
    }

    /**
     * 批量保存数据（同步）
     *
     * @param index
     * @param primaryKeyName
     * @param paramListJson
     * @param clazz
     * @return
     */
    public boolean addBatch(String index, String primaryKeyName, String paramListJson, Class clazz) {
        boolean result = isIndexExists(index);

        if (!result) {
            boolean createResult = createIndexAndCreateMapping(index, EsMappingUtils.getFieldInfo(clazz));
            if (!createResult) {
                log.info("索引[{}]创建失败", index);
                return false;
            }
        }

        BulkRequest bulkRequest = packBulkIndexRequest(index, primaryKeyName, paramListJson);
        try {
            // 同步执行
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]更新操作失败,状态为:[{}],错误信息:{}", index, item.getId(),
                            item.status(), item.getFailureMessage());
                }
                return false;
            }

            // 记录索引新增与修改数量
            Integer createdCount = 0;
            Integer updatedCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.CREATED.equals(item.getResponse().getResult())) {
                    createdCount++;
                } else if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    updatedCount++;
                }
            }
            log.info("索引[{}]批量同步更新成功,共新增[{}]个,修改[{}]个", index, createdCount, updatedCount);
        } catch (IOException e) {
            log.error("索引[{}]批量同步更新出现异常", index);
            return false;
        }
        return true;
    }

    /**
     * 批量保存数据（异步）
     *
     * @param index
     * @param primaryKeyName
     * @param paramListJson
     * @return
     */
    public boolean addBatchAsync(String index, String primaryKeyName, String paramListJson) {
        BulkRequest bulkRequest = packBulkIndexRequest(index, primaryKeyName, paramListJson);
        try {
            //异步执行
            ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
                @Override
                public void onResponse(BulkResponse bulkResponse) {
                    if (bulkResponse.hasFailures()) {
                        for (BulkItemResponse item : bulkResponse.getItems()) {
                            log.error("索引[{}],主键[{}]更新操作失败,状态为:[{}],错误信息:{}", index, item.getId(),
                                    item.status(), item.getFailureMessage());
                        }
                    }
                }

                // 失败操作
                @Override
                public void onFailure(Exception e) {
                    log.error("索引[{}]批量异步更新出现异常:{}", index, e);
                }
            };
            client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, listener);
            log.info("异步批量更新索引[{}]中", index);
        } catch (Exception e) {
            log.info("异步批量更新索引[{}]出现异常:{}", index, e);
            return false;
        }
        return true;
    }

    /**
     * 修改数据
     *
     * @param index
     * @param id
     * @param paramJson
     * @return
     */
    public boolean update(String index, String id, String paramJson) {
        UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.docAsUpsert(true);
        updateRequest.doc(paramJson, XContentType.JSON);
        try {
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
            log.info("索引[{}],主键:[{}]操作结果:[{}]", index, id, updateResponse.getResult());

            if (UpdateResponse.Result.CREATED.equals(updateResponse.getResult())) {
                log.info("索引:[{}],主键:[{}]新增成功", index, id);
                return true;
            } else if (UpdateResponse.Result.UPDATED.equals(updateResponse.getResult())) {
                log.info("索引:[{}],主键:[{}]修改成功", index, id);
                return true;
            } else if (UpdateResponse.Result.NOOP.equals(updateResponse.getResult())) {
                log.info("索引:[{}] 主键:[{}] 无变化", index, id);
                return true;
            }
        } catch (IOException e) {
            log.info("索引:[{}],主键:[{}] 更新异常:{}", index, id, e);
            return false;
        }
        return false;
    }

    /**
     * 批量修改数据(同步)
     *
     * @param index
     * @param primaryKeyName
     * @param paramListJson
     * @return
     */
    public boolean updateBatch(String index, String primaryKeyName, String paramListJson) {
        BulkRequest bulkRequest = packBulkUpdateRequest(index, primaryKeyName, paramListJson);
        try {
            // 同步执行
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("索引[{}],主键[{}]修改操作失败,状态为:[{}],错误信息:{}", index, item.getId(),
                            item.status(), item.getFailureMessage());
                }
                return false;
            }

            Integer createdCount = 0;
            Integer updatedCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (IndexResponse.Result.CREATED.equals(item.getResponse().getResult())) {
                    createdCount++;
                } else if (IndexResponse.Result.UPDATED.equals(item.getResponse().getResult())) {
                    updatedCount++;
                }
            }
            log.info("索引[{}]批量修改更新成功,共新增[{}]个,修改[{}]个", index, createdCount, updatedCount);
        } catch (IOException e) {
            log.error("索引[{}]批量修改更新出现异常", index);
            return false;
        }
        return true;
    }

    /**
     * 通过主键删除数据
     *
     * @param index
     * @param id
     * @return
     */
    public boolean deleteById(String index, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(index);
        deleteRequest.id(id);

        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                log.error("索引[{}]主键[{}]删除失败", index, id);
                return false;
            } else {
                log.info("索引[{}]主键[{}]删除成功", index, id);
                return true;
            }
        } catch (IOException e) {
            log.error("删除索引[{}]出现异常[{}]", index, e);
            return false;
        }
    }

    /**
     * 批量删除(同步)
     *
     * @param index
     * @param ids
     * @return
     */
    public boolean bulkDelete(String index, List<String> ids) {
        BulkRequest bulkRequest = packBulkDeleteRequest(index, ids);

        try {
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.hasFailures()) {
                for (BulkItemResponse item : bulk.getItems()) {
                    log.error("更新索引：{}，主键：{}失败。信息为：{}", index, item.getId(), item.getFailureMessage());
                }
                return false;
            }

            Integer deleteCount = 0;
            for (BulkItemResponse item : bulk.getItems()) {
                if (DeleteResponse.Result.DELETED.equals(item.getResponse().getResult())) {
                    deleteCount++;
                }
            }
            log.info("批量删除索引[{}]成功,共删除[{}]个", index, deleteCount);
        } catch (IOException e) {
            log.error("删除索引：{}批量保存数据出现异常:{}", index, e);
            return false;
        }
        return true;
    }

    public boolean deleteIndex(String index) {
        boolean result = isIndexExists(index);
        if (!result) {
            log.error("索引[{}]不存在删除索引失败", index);
            return false;
        }
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
            AcknowledgedResponse acknowledgedResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            if (!acknowledgedResponse.isAcknowledged()) {
                log.error("索引[{}]删除失败", index);
            }
            log.info("索引[{}]删除成功", index);
            return true;
        } catch (IOException e) {
            log.error("索引[{}]删除异常:{}", index, e);
        }
        return false;
    }

    public String getById(String index, String id) {
        if (!isIndexExists(index)) {
            return "";
        }
        GetRequest getRequest = new GetRequest(index, id);
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            String resultJson = getResponse.getSourceAsString();
            log.debug("索引[{}]主键[{}]查询结果[{}]", index, id, resultJson);
            return resultJson;
        } catch (IOException e) {
            log.debug("索引[{}]主键[{}]查询异常:{}", index, id, e);
            return "";
        }
    }

    /**
     * 搜索 支持多种搜索方式（分页、区间、模糊、OR、IN、过滤）
     **/
    public PageResult query(EsParamDto paramDto, String index) {
        PageResult pageResult = new PageResult();
        if (!isIndexExists(index)) {
            return pageResult;
        }
        // 获取页码、页面大小
        int pageStart = 0;
        int pageSize = 1000;
        if (paramDto.getIsPage()) {
            pageSize = paramDto.getSize();
            pageStart = (paramDto.getPage() - 1) * pageSize;
        }

        // 封装boolBuilder
        QueryBuilder boolBuilder = queryBuilder(paramDto);

        // 封装SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                // 设置查询关键词
                .query(boolBuilder)
                // 设置查询数据的位置,分页用
                .from(pageStart)
                // 设置查询结果集的最大条数
                .size(pageSize)
                // 不展示分析逻辑
                .explain(false)
                // 配置高亮
                .highlighter(this.getHighlightBuilder(paramDto));

        //指定查询包含的字段，指定查询不包含的字段
        if (paramDto.getFetchSourceIncludes() != null || paramDto.getFetchSourceExcludes() != null) {
            searchSourceBuilder.fetchSource(paramDto.getFetchSourceIncludes(), paramDto.getFetchSourceExcludes());
        }

        //排序list，以安排排序的优先级顺序
        if(!CollectionUtils.isEmpty(paramDto.getSortFiledsList())) {
            for (Map<String, String> map : paramDto.getSortFiledsList()) {
                for (String sortKey : map.keySet()) {
                    if (EsConstant.SORT_ASC.equalsIgnoreCase(paramDto.getSortFileds(sortKey))) {
                        searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.ASC));
                    } else if (EsConstant.SORT_DESC.equalsIgnoreCase(paramDto.getSortFileds(sortKey))) {
                        searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.DESC));
                    }
                }
            }
        }

        // 设置排序字段
        if(!CollectionUtils.isEmpty(paramDto.getSortFileds())) {
            for (String sortKey : paramDto.getSortFileds().keySet()) {
                if (EsConstant.SORT_ASC.equalsIgnoreCase(paramDto.getSortFileds(sortKey))) {
                    searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.ASC));
                } else if (EsConstant.SORT_DESC.equalsIgnoreCase(paramDto.getSortFileds(sortKey))) {
                    searchSourceBuilder.sort(SortBuilders.fieldSort(sortKey).order(SortOrder.DESC));
                }
            }
        }

        if (StringUtils.isNotBlank(paramDto.getCollapseField())) {
            searchSourceBuilder.collapse(new CollapseBuilder(paramDto.getCollapseField()));
        }

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);

        // 设置查询类型
        // 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
        // 2.SearchType.SCAN = 扫描查询,无序
        // 3.SearchType.COUNT = 不设置的话,这个为默认值,
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            paramDto.setTotal(searchHits.getTotalHits().value);
            log.debug("共匹配到:" + searchHits.getTotalHits().value + "条记录!");

            SearchHit[] hits = searchHits.getHits();
            pageResult.setPage(pageStart);
            pageResult.setSize(pageSize);
            pageResult.setTotal(searchHits.getTotalHits().value);
            List<Object> eventEsResultList = new ArrayList<>();
            for (SearchHit searchHit : hits) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

                //替换高亮文字
                if (!CollectionUtils.isEmpty(paramDto.getHightFieldList())) {
                    // 获取对应的高亮域
                    Map<String, HighlightField> highlight = searchHit.getHighlightFields();
                    for (String field : paramDto.getHightFieldList()) {
                        // 从设定的高亮域中取得指定域
                        HighlightField titleField = highlight.get(field);
                        if (titleField == null) {
                            continue;
                        }
                        // 取得定义的高亮标签
                        String texts = org.apache.commons.lang3.StringUtils.join(titleField.fragments());
                        sourceAsMap.put(field, texts);
                    }
                    sourceAsMap.put("item", searchHit.getId());
                }
                eventEsResultList.add(sourceAsMap);
            }
            pageResult.setRows(eventEsResultList);
        } catch (IOException e) {
            log.error("查询索引[{}]数据出现异常{}", index, e);
            return pageResult;
        }

        return pageResult;
    }

    public void close() throws IOException {
        client.close();
    }

    private BoolQueryBuilder queryBuilder(EsParamDto paramDto) {
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        // 区间查询
        sectionSearch(paramDto, boolBuilder);
        // 模糊查询
        likeSearch(paramDto, boolBuilder);

        // or拼接查询
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        Map<String, Object> orSearchCondition = paramDto.getOrSearchCondition();
        filterOrQuery(orSearchCondition, orBoolQueryBuilder);
        boolBuilder.must(orBoolQueryBuilder);

        // and (or)拼接查询
        if(!CollectionUtils.isEmpty(paramDto.getOrSearchConditionList())) {
            for (Map<String, Object> searchCondition : paramDto.getOrSearchConditionList()) {
                BoolQueryBuilder termBoolQueryBuilder = QueryBuilders.boolQuery();
                filterOrQuery(searchCondition, termBoolQueryBuilder);
                boolBuilder.must(termBoolQueryBuilder);
            }
        }

        // in查询
        if(!CollectionUtils.isEmpty(paramDto.getInSearchCondition())) {
            for (String key : paramDto.getInSearchCondition().keySet()) {
                BoolQueryBuilder ptermsBoolQueryBuilder = QueryBuilders.boolQuery();
                TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery(key, paramDto.getInSearchCondition().get(key));
                ptermsBoolQueryBuilder.should(termQueryBuilder);
                TermsQueryBuilder termQueryBuilder2 = QueryBuilders.termsQuery(key + ".keyword", paramDto.getInSearchCondition().get(key));
                ptermsBoolQueryBuilder.should(termQueryBuilder2);
                boolBuilder.must(ptermsBoolQueryBuilder);
            }
        }

        if(!CollectionUtils.isEmpty(paramDto.getIsNullConditioin())) {
            // 为空匹配
            for (String key : paramDto.getIsNullConditioin()) {
                ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery(key);
                boolBuilder.mustNot(existsQueryBuilder);
            }
        }
        // 非空匹配
        if(!CollectionUtils.isEmpty(paramDto.getIsNullConditioin())) {
            for (String key : paramDto.getIsNullConditioin()) {
                ExistsQueryBuilder existsQueryBuilder = QueryBuilders.existsQuery(key);
                boolBuilder.must(existsQueryBuilder);
            }
        }
        return boolBuilder;
    }


    private BoolQueryBuilder sectionSearch(EsParamDto paramDto, BoolQueryBuilder boolBuilder) {
        // 时间区间查询
        if (!CollectionUtils.isEmpty(paramDto.getRangConditionsToTimeModelMap())) {
            for (String key : paramDto.getRangConditionsToTimeModelMap().keySet()) {
                RangConditionsToTimeModelDTO rangConditionsToTimeModelMap = paramDto.getRangConditionsToTimeModelMap(key);
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
                if (null != rangConditionsToTimeModelMap.getBeginTime()) {
                    rangeQueryBuilder.gte(rangConditionsToTimeModelMap.getBeginTime().getTime());
                }
                if (null != rangConditionsToTimeModelMap.getEndTime()) {
                    rangeQueryBuilder.lte(rangConditionsToTimeModelMap.getEndTime().getTime());
                }

                // 包括下界
                rangeQueryBuilder.includeLower(true);
                // 包括上界
                rangeQueryBuilder.includeUpper(false);
                boolBuilder.must(rangeQueryBuilder);
                log.debug("rangeQueryBuilder:" + rangeQueryBuilder);
            }
        }

        // 其他数据区间查询
        if (!CollectionUtils.isEmpty(paramDto.getRangConditionMap())) {
            for (String key : paramDto.getRangConditionMap().keySet()) {
                RangConditionDTO rangConditionMap = paramDto.getRangConditionMap(key);
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(key);
                if (StringUtils.isNotBlank(rangConditionMap.getBeginValue())) {
                    rangeQueryBuilder.gte(rangConditionMap.getBeginValue());
                }
                if (StringUtils.isNotBlank(rangConditionMap.getEndValue())) {
                    rangeQueryBuilder.lte(rangConditionMap.getEndValue());

                }
                // 包括下界
                rangeQueryBuilder.includeLower(true);
                // 包括上界
                rangeQueryBuilder.includeUpper(false);
                boolBuilder.must(rangeQueryBuilder);
                log.debug("rangeQueryBuilder:" + rangeQueryBuilder);
            }
        }
        return boolBuilder;
    }

    private BoolQueryBuilder likeSearch(EsParamDto paramDto, BoolQueryBuilder boolBuilder) {
        // 模糊匹配查询
        if(!CollectionUtils.isEmpty(paramDto.getLikeSearchCondition())) {
            for (String key : paramDto.getLikeSearchCondition().keySet()) {
                Object machValue = paramDto.getLikeSearchCondition().get(key);
                if (null == machValue) {
                    continue;
                }
                BoolQueryBuilder wildcardBoolQueryBuilder = QueryBuilders.boolQuery();
                for (String value : machValue.toString().split(EsConstant.SPLIT_FLAG)) {

                    WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(key, "*" + value + "*");
                    wildcardBoolQueryBuilder.should(wildcardQueryBuilder);
                }
                boolBuilder.must(wildcardBoolQueryBuilder);
            }
        }
        // 精确匹配查询
        if(!CollectionUtils.isEmpty(paramDto.getEqualsSearchCondition())) {
            for (String key : paramDto.getEqualsSearchCondition().keySet()) {
                Object machValue = paramDto.getEqualsSearchCondition().get(key);
                if (null == machValue) {
                    continue;
                }
                BoolQueryBuilder ptermBoolQueryBuilder = QueryBuilders.boolQuery();
                filterQuery(key, machValue, ptermBoolQueryBuilder);
                boolBuilder.must(ptermBoolQueryBuilder);

            }
        }
        // 精确过滤查询
        if(!CollectionUtils.isEmpty(paramDto.getNoEqualsSearchConditioin())) {
            for (String key : paramDto.getNoEqualsSearchConditioin().keySet()) {
                Object noMachValue = paramDto.getNoEqualsSearchConditioin().get(key);
                if (null == noMachValue) {
                    continue;
                }
                BoolQueryBuilder noBoolQueryBuilder = QueryBuilders.boolQuery();

                filterQuery(key, noMachValue, noBoolQueryBuilder);

                boolBuilder.mustNot(noBoolQueryBuilder);
            }
        }
        //模糊过滤查询
        if(!CollectionUtils.isEmpty(paramDto.getNoLikeSearchConditioin())) {
            for (String key : paramDto.getNoLikeSearchConditioin().keySet()) {
                Object noMachValue = paramDto.getNoLikeSearchConditioin().get(key);
                BoolQueryBuilder noBoolQueryBuilder = QueryBuilders.boolQuery();
                if (null == noMachValue) {
                    continue;
                }
                for (String value : noMachValue.toString().split(EsConstant.SPLIT_FLAG)) {
                    WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(key, value);
                    noBoolQueryBuilder.should(wildcardQueryBuilder);
                }
                boolBuilder.mustNot(noBoolQueryBuilder);

            }
        }
        return boolBuilder;
    }

    private void filterQuery(String key, Object noMachValue, BoolQueryBuilder noBoolQueryBuilder) {
        for (String cv : noMachValue.toString().split(EsConstant.SPLIT_FLAG)) {
            BoolQueryBuilder termBoolQueryBuilder = QueryBuilders.boolQuery();
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key + ".keyword", cv);
            termBoolQueryBuilder.should(termQueryBuilder);
            TermQueryBuilder longtermQueryBuilder = QueryBuilders.termQuery(key, cv);
            termBoolQueryBuilder.should(longtermQueryBuilder);
            noBoolQueryBuilder.should(termBoolQueryBuilder);
        }
    }

    private void filterOrQuery(Map<String, Object> orSearchCondition, BoolQueryBuilder termBoolQueryBuilder) {

        if(!CollectionUtils.isEmpty(orSearchCondition)) {
            for (String key : orSearchCondition.keySet()) {
                Object value = orSearchCondition.get(key);
                if (null == value) {
                    continue;
                }
                BoolQueryBuilder sueryBuilder = QueryBuilders.boolQuery();
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key + ".keyword", value);
                sueryBuilder.should(termQueryBuilder);
                TermQueryBuilder longtermQueryBuilder = QueryBuilders.termQuery(key, value);
                sueryBuilder.should(longtermQueryBuilder);
                termBoolQueryBuilder.should(sueryBuilder);
            }
        }
    }


    private HighlightBuilder getHighlightBuilder(EsParamDto paramDto) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'>");
        highlightBuilder.postTags("</span>");

        List<String> fields = paramDto.getHightFieldList();
        if (!CollectionUtils.isEmpty(fields)) {
            for (String field : fields) {
                highlightBuilder.field(field);
            }
        }
        return highlightBuilder;
    }


    private BulkRequest packBulkDeleteRequest(String index, List<String> ids) {
        BulkRequest bulkRequest = new BulkRequest();

        ids.forEach(id -> {
            DeleteRequest deleteRequest = new DeleteRequest(index);
            deleteRequest.id(id);
            bulkRequest.add(deleteRequest);
        });
        return bulkRequest;
    }

    private boolean isIndexExists(String index) {
        boolean exists = false;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(index);
            getIndexRequest.humanReadable(true);
            exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("判断索引:{} 是否存在异常，异常内容:{}", index, e);
        }
        return exists;
    }

    private boolean createIndexAndCreateMapping(String index, List<EsMapping> esMappingList) {
        try {

            XContentBuilder mapping = packESMapping(esMappingList, null);
            mapping.endObject().endObject();

            // 进行索引的创建
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            createIndexRequest.mapping(mapping);
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            if (acknowledged) {
                log.info("索引:{}创建成功", index);
                return true;
            } else {
                log.error("索引:{}创建失败", index);
                return false;
            }
        } catch (IOException e) {
            log.error("创建索引：{}，出现异常：{}", index, e);
            return false;
        }
    }

    private XContentBuilder packESMapping(List<EsMapping> esMappingList, XContentBuilder mapping) throws
            IOException {

        if (mapping == null) {
            // 如果对象是空，首次进入，设置开始节点
            mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties");
        }

        for (EsMapping info : esMappingList) {
            String field = info.getField();
            String dateType = info.getType();

            // 类型为空默认设置为string
            if (StringUtils.isBlank(dateType)) {
                dateType = "string";
            }
            dateType = dateType.toLowerCase();
            int participle = info.getParticiple();
            if ("string".equals(dateType)) {
                // 设置分词规则
                if (participle == 0) {
                    mapping.startObject(field)
                            .field("type", "keyword")
                            .endObject();
                } else if (participle == 1) {
                    mapping.startObject(field)
                            .field("type", "text")
                            .field("analyzer", "ik_smart")
                            .endObject();
                } else if (participle == 2) {
                    mapping.startObject(field)
                            .field("type", "text")
                            .field("analyzer", "ik_max_word")
                            .endObject();
                }
            } else if ("text".equals(dateType)) {
                mapping.startObject(field).field("fielddata", true)
                        .field("type", dateType).startObject("fields").startObject("keyword")
                        .field("ignore_above", 256).field("type", "keyword")
                        .endObject().endObject().endObject();
            } else if ("date".equals(dateType)) {
                // 设置时间格式
                mapping.startObject(field)
                        .field("type", dateType)
                        .field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
                        .endObject();
            } else if ("float".equals(dateType) || "double".equals(dateType)) {
                // 设置小数类型
                mapping.startObject(field)
                        .field("type", "scaled_float")
                        .field("scaling_factor", 100)
                        .endObject();
            } else if ("nested".equals(dateType)) {
                // 设置聚合类型
                mapping.startObject(field)
                        .field("type", dateType)
                        .startObject("properties");
                // 由于nested类型内嵌文档，因此需要封装内嵌文档的Mapping
                mapping = packESMapping(info.getEsMappingList(), mapping);
                // 设置尾部节点
                mapping.endObject().endObject();
            } else {
                // object类型
                if (info.getEsMappingList() != null) {
                    // 内嵌文档循环封装（Java类对象中嵌套对象）
                    mapping.startObject(field).startObject("properties");
                    mapping = packESMapping(info.getEsMappingList(), mapping);
                    mapping.endObject().endObject();
                } else {
                    // 常量类型配置
                    mapping.startObject(field)
                            .field("type", dateType)
                            .field("index", true)
                            .endObject();
                }
            }
        }

        return mapping;
    }

    private BulkRequest packBulkIndexRequest(String index, String primaryKeyName, String paramListJson) {
        BulkRequest bulkRequest = new BulkRequest();
        JSONArray jsonArray = JSONArray.parseArray(paramListJson);

        if (jsonArray == null) {
            return bulkRequest;
        }

        // 循环数据封装bulkRequest
        jsonArray.forEach(obj -> {
            Map<String, Object> map = (Map<String, Object>) obj;
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.id(String.valueOf(map.get(primaryKeyName)));
            indexRequest.source(JSON.toJSONString(obj), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        return bulkRequest;
    }

    private BulkRequest packBulkUpdateRequest(String index, String primaryKeyName, String paramListJson) {
        BulkRequest bulkRequest = new BulkRequest();
        JSONArray jsonArray = JSONArray.parseArray(paramListJson);
        if (jsonArray == null) {
            return bulkRequest;
        }

        jsonArray.forEach(obj -> {
            Map<String, Object> map = (Map<String, Object>) obj;
            UpdateRequest updateRequest = new UpdateRequest(index, String.valueOf(map.get(primaryKeyName)));
            updateRequest.docAsUpsert(true);
            updateRequest.doc(JSON.toJSONString(obj), XContentType.JSON);
            bulkRequest.add(updateRequest);
        });
        return bulkRequest;
    }

}
