package com.study.fun.client.util;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUrlParser {
    public HttpUrlParser() {
    }

    public static String buildUrl(String urlPattern, Map<String, Object> variableMap) {
        return stringFormat(urlPattern, variableMap);
    }

    public static String stringFormat(String format, Map<String, Object> variableMap) {
        if (StringUtils.isBlank(format)) {
            return format;
        } else if (MapUtils.isEmpty(variableMap)) {
            return format;
        } else {
            Pattern pattern = Pattern.compile("\\{(.*?)}");

            String point;
            for(Matcher matcher = pattern.matcher(format); matcher.find(); format = format.replace("{" + point + "}", variableMap.get(point).toString())) {
                point = matcher.group(1);
                if (!Objects.nonNull(variableMap.get(point))) {
                    throw new RuntimeException("缺少url路径中[" + point + "]值参数,请在pathMap中设置");
                }
            }

            return format;
        }
    }
}
