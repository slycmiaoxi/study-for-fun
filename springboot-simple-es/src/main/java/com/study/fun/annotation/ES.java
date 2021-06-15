package com.study.fun.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ES {

    /**
     * @string text ,keyword
     * @Number long, integer, short, byte, double, float, half_float, scaled_float
     * @date date
     * @date_nanos date_nanos
     * @Range integer_range, float_range, long_range, double_range, date_range
     * @binary binary
     * @Nested nested
     */
    String type() default "string";

    /**
     * 分词器选择  0. not_analyzed   1. ik_smart 2. ik_max_word
     */
    int participle() default 0;

}
