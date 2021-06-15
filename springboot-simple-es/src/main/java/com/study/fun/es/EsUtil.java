package com.study.fun.es;

import com.study.fun.dto.EsParamDto;
import com.study.fun.model.PageResult;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.List;

public class EsUtil {

    private static GenericObjectPool<EsModel> pool;

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);// 整个池最大值
        config.setMaxIdle(10);// 最大空闲
        config.setMinIdle(0);// 最小空闲
        config.setMaxWaitMillis(-1);// 最大等待时间，-1表示一直等
        config.setBlockWhenExhausted(true);// 当对象池没有空闲对象时，新的获取对象的请求是否阻塞。true阻塞。默认值是true
        config.setTestOnBorrow(false);// 在从对象池获取对象时是否检测对象有效，true是；默认值是false
        config.setTestOnReturn(false);// 在向对象池中归还对象时是否检测对象有效，true是，默认值是false
        config.setTestWhileIdle(true);// 在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。true是，默认值是false
        config.setMinEvictableIdleTimeMillis(10 * 60000L); // 可发呆的时间,10mins
        config.setTestWhileIdle(true); // 发呆过长移除的时候是否test一下先

        pool = new GenericObjectPool<EsModel>(new EsFactory(), config);
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
    public static boolean add(String index, String id, String paramJson, Class clazz) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.add(index, id, paramJson, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
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
    public static boolean addBatch(String index, String primaryKeyName, String paramListJson, Class clazz) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.addBatch(index, primaryKeyName, paramListJson, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return false;
    }

    /**
     * 批量保存数据（异步）
     *
     * @param index
     * @param primaryKeyName
     * @param paramListJson
     * @return
     */
    public static boolean addBatchAsync(String index, String primaryKeyName, String paramListJson) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.addBatchAsync(index, primaryKeyName, paramListJson);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return false;
    }

    /**
     * 修改数据
     *
     * @param index
     * @param id
     * @param paramJson
     * @return
     */
    public static boolean update(String index, String id, String paramJson) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.update(index, id, paramJson);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
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
    public static boolean updateBatch(String index, String primaryKeyName, String paramListJson) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.updateBatch(index, primaryKeyName, paramListJson);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return false;
    }

    /**
     * 通过主键删除数据
     *
     * @param index
     * @param id
     * @return
     */
    public static boolean deleteById(String index, String id) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.deleteById(index, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return false;
    }

    /**
     * 批量删除(同步)
     *
     * @param index
     * @param ids
     * @return
     */
    public static boolean bulkDelete(String index, List<String> ids) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.bulkDelete(index, ids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public static boolean deleteIndex(String index) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.deleteIndex(index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return false;
    }

    /**
     * 通过id获取doc
     *
     * @param index
     * @param id
     * @return
     */
    public static String getById(String index, String id) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.getById(index, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return null;
    }

    /**
     * 搜索 支持多种搜索方式（分页、区间、模糊、OR、IN、过滤）
     *
     * @param paramDto
     * @param index
     * @return
     */
    public static PageResult query(EsParamDto paramDto, String index) {
        EsModel es = null;
        try {
            es = pool.borrowObject();
            return es.query(paramDto, index);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(es);
        }
        return null;
    }


}
