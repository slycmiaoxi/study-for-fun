package com.study.fun.clickhouse;


import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


public class ClickHouseFactory extends BasePooledObjectFactory<ClickHouse> {
    @Override
    public ClickHouse create() throws Exception {
        return new ClickHouse();
    }

    @Override
    public PooledObject<ClickHouse> wrap(ClickHouse arg0) {
        return new DefaultPooledObject<ClickHouse>(arg0);
    }

    @Override
    public void destroyObject(PooledObject<ClickHouse> p) throws Exception {
        p.getObject().close();
        super.destroyObject(p);
    }
}
