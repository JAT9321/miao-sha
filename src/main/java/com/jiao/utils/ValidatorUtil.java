package com.jiao.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * auth:@highSky
 * create:2022/8/12
 * email:zgt9321@qq.com
 **/
public class ValidatorUtil {
    private static final Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");

    public static boolean isMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        return mobile_pattern.matcher(mobile).matches();
    }

}
