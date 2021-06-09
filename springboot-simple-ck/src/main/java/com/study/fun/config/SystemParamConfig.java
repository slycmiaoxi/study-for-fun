package com.study.fun.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(
        prefix = "system.param"
)
@Configuration
public class SystemParamConfig {

    private SystemParamConfig.DataStorageType dataStorageType;

    public SystemParamConfig() {
        this.dataStorageType = DataStorageType.mysql;
    }

    public DataStorageType getDataStorageType() {
        return dataStorageType;
    }

    public void setDataStorageType(SystemParamConfig.DataStorageType dataStorageType) {
        this.dataStorageType = dataStorageType;
    }

    public static enum DataStorageType {
        mysql,
        clickhouse;

        private DataStorageType() {
        }
    }
}
