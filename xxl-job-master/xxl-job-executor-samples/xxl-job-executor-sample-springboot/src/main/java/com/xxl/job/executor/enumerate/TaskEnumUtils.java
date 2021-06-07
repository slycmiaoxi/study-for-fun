package com.xxl.job.executor.enumerate;


import java.util.EnumMap;


public class TaskEnumUtils {

    /**
     * <p>
     * sys_task_datetime配置的api_name
     * </p>
     */
    public enum taskDatetimeType {
        TOKEN("token"), LEESIN("leesin"), YURNERO("yurnero");

        public String key;

        private taskDatetimeType(String key) {
            this.key = key;
        }
    }

    public static EnumMap taskDatetimeTypeEnum = new EnumMap(taskDatetimeType.class) {
        {
            put(taskDatetimeType.TOKEN, "token");
            put(taskDatetimeType.LEESIN, "盲僧接口");
            put(taskDatetimeType.YURNERO, "剑圣接口");
        }
    };

    public enum taskApiUrlType {
        LEESIN("leesin"), YURNERO("yurnero");

        public String key;

        private taskApiUrlType(String key) {
            this.key = key;
        }

        public static String getValueByKey(String key) {
            for (taskApiUrlType item : values()) {
                if (item.key.equals(key)) {
                    return (String) taskApiUrlTypeEnum.get(item);
                }
            }
            return null;
        }
    }

    public static EnumMap taskApiUrlTypeEnum = new EnumMap(taskApiUrlType.class) {
        {
            put(taskApiUrlType.LEESIN, "getinformation/leesinlist");
            put(taskApiUrlType.YURNERO, "getinformation/yurnerolist");
        }
    };

}
