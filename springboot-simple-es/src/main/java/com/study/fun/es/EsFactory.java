package com.study.fun.es;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class EsFactory extends BasePooledObjectFactory<EsModel> {
    @Override
    public EsModel create() throws Exception {
        return new EsModel();
    }

    @Override
    public PooledObject<EsModel> wrap(EsModel arg0) {
        return new DefaultPooledObject<EsModel>(arg0);
    }

    @Override
    public void destroyObject(PooledObject<EsModel> p) throws Exception {
        p.getObject().close();
        super.destroyObject(p);
    }

}
