package com.xxl.job.executor.utils;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;


public class JoinerUtil {

    public static String joinEquals(String first, String second, String... rest) {
        if (StringUtils.isEmpty(first) || StringUtils.isEmpty(second)) {
            return "";
        }

        return Joiner.on("=").join(first, second, rest);
    }

    public static String joinAnd(String first, String second, String... rest) {
        if (StringUtils.isEmpty(first) || StringUtils.isEmpty(second)) {
            return "";
        }

        return Joiner.on("&").join(first, second, rest);
    }

    public static String joinQuestion(String first, String second, String... rest) {
        if (StringUtils.isEmpty(first) || StringUtils.isEmpty(second)) {
            return "";
        }

        return Joiner.on("?").join(first, second, rest);
    }
}
