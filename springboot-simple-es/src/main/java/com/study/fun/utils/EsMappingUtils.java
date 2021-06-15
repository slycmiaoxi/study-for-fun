package com.study.fun.utils;

import com.study.fun.annotation.ES;
import com.study.fun.model.EsMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class EsMappingUtils {

    public static List<EsMapping> getFieldInfo(Class clazz) {
        return getFieldInfo(clazz, null);
    }



    public static List<EsMapping> getFieldInfo(Class clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        List<EsMapping> esMappingList = new ArrayList<>();

        for (Field field : fields) {
            ES es = field.getAnnotation(ES.class);
            if (es == null) {
                continue;
            }

            if ("object".equals(es.type()) || "nested".equals(es.type())) {
                Class fc = field.getType();
                if (fc.isPrimitive()) {
                    String name = field.getName();
                    if (StringUtils.isNotBlank(fieldName)) {
                        name = name + "." + fieldName;
                    }
                    esMappingList.add(new EsMapping(name, es.type(), es.participle()));
                } else {
                    if (fc.isAssignableFrom(List.class)) {
                        log.debug("List类型：{}", field.getName());
                        // 得到泛型类型
                        Type gt = field.getGenericType();
                        ParameterizedType pt = (ParameterizedType) gt;
                        Class listClass = (Class) pt.getActualTypeArguments()[0];

                        // 循环获取集合里面的实体FieldMapping
                        List<EsMapping> esMappings = getFieldInfo(listClass, field.getName());
                        EsMapping esMapping = new EsMapping(field.getName(), es.type(), es.participle());
                        esMapping.setEsMappingList(esMappings);
                        esMappingList.add(esMapping);
                    } else {
                        // 循环获取集合里面的实体FieldMapping
                        List<EsMapping> esMappings = getFieldInfo(fc, field.getName());
                        EsMapping esMapping = new EsMapping(field.getName(), es.type(), es.participle());
                        esMapping.setEsMappingList(esMappings);
                        esMappingList.add(esMapping);
                    }
                }

            } else {
                String name = field.getName();
                esMappingList.add(new EsMapping(name, es.type(), es.participle()));
            }

        }
        return esMappingList;

    }
}

