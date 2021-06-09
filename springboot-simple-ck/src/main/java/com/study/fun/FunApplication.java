package com.study.fun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.study.fun.mapper"})
public class FunApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunApplication.class, args);
    }

}
