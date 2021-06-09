package com.study.fun.config;


import com.study.fun.service.api.IApiLeesinService;
import com.study.fun.service.strategy.DataStrategyService;
import com.study.fun.service.strategy.impl.CkDataStrategyServiceImpl;
import com.study.fun.service.strategy.impl.MysqlDataStrategyServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyManagerConfig {

    private final SystemParamConfig paramConfig;

    private final IApiLeesinService apiLeesinService;


    @Bean
    @ConditionalOnMissingBean({DataStrategyService.class})
    public DataStrategyService dataStrategyService() {
        SystemParamConfig.DataStorageType dataStorageType = this.paramConfig.getDataStorageType();
        Object dataStrategyService;
        switch (dataStorageType) {
            case mysql:
                dataStrategyService = new MysqlDataStrategyServiceImpl(apiLeesinService);
                break;
            case clickhouse:
                dataStrategyService = new CkDataStrategyServiceImpl();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dataStorageType);
        }

        return (DataStrategyService) dataStrategyService;
    }

    public StrategyManagerConfig(final SystemParamConfig paramConfig, final IApiLeesinService apiLeesinService) {
        this.paramConfig = paramConfig;
        this.apiLeesinService = apiLeesinService;
    }

}
