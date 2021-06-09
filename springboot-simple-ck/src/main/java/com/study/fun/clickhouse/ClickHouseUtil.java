package com.study.fun.clickhouse;


import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ClickHouseUtil {

    private static GenericObjectPool<ClickHouse> pool;

    static {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(10);
        config.setMinIdle(0);
        config.setMaxWaitMillis(-1);
        config.setBlockWhenExhausted(true);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(10 * 60000L);
        config.setTestWhileIdle(true);
        pool = new GenericObjectPool<ClickHouse>(new ClickHouseFactory(), config);
    }


    public static List<Map<String, String>> exeSql(String sql) {
        ClickHouse ck = null;
        try {
            ck = pool.borrowObject();
            return ck.exeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(ck);
        }
        return new ArrayList<>();
    }


    public static void insertBatch(List<?> datas, String tableName, Class cls) {
        ClickHouse ck = null;
        try {
            ck = pool.borrowObject();
            ck.insertBatch(datas, tableName, cls);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(ck);
        }
    }

    public static void updateBatch(List<?> datas, String tableName) {
        ClickHouse ck = null;
        try {
            ck = pool.borrowObject();
            ck.updateBatch(datas, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.returnObject(ck);
        }
    }
}
