package com.study.fun.model;


import com.study.fun.annotation.ES;
import lombok.Data;

import java.io.Serializable;

@Data
public class EsTestModel implements Serializable {

    private String id;

    private String userName;

    private String password;

    @ES(type = "integer")
    private int age;
}
