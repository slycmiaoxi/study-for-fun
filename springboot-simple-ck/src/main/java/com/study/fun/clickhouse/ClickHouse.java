package com.study.fun.clickhouse;


import cn.hutool.core.date.DateUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;


@Slf4j
public class ClickHouse {

    private static Connection conn;

    private static String clickhouseAddress = "jdbc:clickhouse://localhost:8123";

    private static String clickhouseUsername = "default";

    private static String clickhousePassword = "";

    private static String clickhouseDB = "default";

    private static Integer clickhouseSocketTimeout = 600000;

    public ClickHouse() {
        ClickHouseProperties properties = new ClickHouseProperties();
        properties.setDatabase(clickhouseDB);
        properties.setUser(clickhouseUsername);
        properties.setPassword(clickhousePassword);
        properties.setSocketTimeout(clickhouseSocketTimeout);
        ClickHouseDataSource clickHouseDataSource = new ClickHouseDataSource(clickhouseAddress, properties);
        try {
            conn = clickHouseDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Map<String, String>> exeSql(String sql) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            ResultSet results = statement.executeQuery(sql);
            List<Map<String, String>> list = new ArrayList<>();
            ResultSetMetaData rsmd = results.getMetaData();
            while (results.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    row.put(rsmd.getColumnName(i), results.getString(rsmd.getColumnName(i)));
                }
                list.add(row);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void insertBatch(List<?> datas, String tableName, Class cls) {
        PreparedStatement statement;
        try {
            conn.setAutoCommit(false);
            //1 拼接插入占位符sql
            String sqlBegin = "insert into " + tableName + "(";
            Field[] fields = cls.getDeclaredFields();
            String params = "";
            String quesion = "?";
            for (int i = 0; i < fields.length; i++) {
                if (i == 0) {
                    params = fields[0].getName();
                    quesion = "?";
                } else {
                    params = Joiner.on(",").join(params, fields[i].getName());
                    quesion = Joiner.on(",").join(quesion, "?");
                }
            }

            String result = Joiner.on("").join(sqlBegin, params, ") VALUES(", quesion, ")");
            statement = conn.prepareStatement(result);
            for (int i = 0; i < datas.size(); i++) {
                //2 反射获取相应字段的值
                Object object = datas.get(i);
                Class<?> targetCls = object.getClass();
                Field[] objectField = targetCls.getDeclaredFields();
                for (int j = 0; j < objectField.length; j++) {
                    Method m = targetCls.getMethod("get" + getMethodName(objectField[j].getName()));
                    Object value = m.invoke(object);
                    value = ensureSafe(value, objectField[j]);
                    statement.setObject(j + 1, value);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBatch(List<?> datas, String tableName) {
        PreparedStatement statement;
        try {
            for (int i = 0; i < datas.size(); i++) {
                String sqlBegin = "alter table " + tableName + " update ";
                String quesion = "";
                String params = "";
                Object object = datas.get(i);
                Class<?> targetCls = object.getClass();
                Field[] objectField = targetCls.getDeclaredFields();
                Method m0 = targetCls.getMethod("get" + getMethodName(objectField[0].getName()));
                Object value0 = m0.invoke(object);

                //主键不更新
                for (int k = 1; k < objectField.length; k++) {
                    quesion = Joiner.on(" = ").join(objectField[k].getName(), "?");
                    if (k != objectField.length - 1) {
                        params = Joiner.on("").join(params, quesion, ", ");
                    } else {
                        sqlBegin = Joiner.on("").join(sqlBegin, params, quesion, "  where ", objectField[0].getName()
                                , " = ", "'" + value0 + "'");
                    }
                }
                statement = conn.prepareStatement(sqlBegin);
                for (int j = 1; j < objectField.length; j++) {
                    Method m1 = targetCls.getMethod("get" + getMethodName(objectField[j].getName()));
                    Object value = m1.invoke(object);
                    value = ensureSafe(value, objectField[j]);
                    statement.setObject(j, value);
                }
                statement.addBatch();
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() throws SQLException {
        conn.close();
    }

    private static Object ensureSafe(Object value, Field field) {
        String type = field.getGenericType().getTypeName();
        if (value != null) {
            return value;
        }

        switch (type) {
            case "java.lang.String":
                value = "";
                break;
            case "java.lang.Integer":
                value = 0;
                break;
            case "java.lang.Double":
                value = 0d;
                break;
            case "java.lang.Boolean":
                value = false;
                break;
            case "java.util.Date":
                String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                value = DateUtil.parse(format, "yyyy-MM-dd HH:mm:ss");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return value;
    }

    private static String getMethodName(String str) {
        char[] cs = str.toCharArray();
        if (cs[0] >= 97 && cs[0] <= 122) {
            cs[0] -= 32;
        }
        return String.valueOf(cs);
    }
}
