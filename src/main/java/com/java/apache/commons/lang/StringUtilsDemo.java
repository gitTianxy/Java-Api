package com.java.apache.commons.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by kevintian on 2017/9/26.
 */
public class StringUtilsDemo {
    public static void main(String[] args) {
        // center
        System.out.println(StringUtils.center("h", 10));
        System.out.println(StringUtils.center("h", 10, "="));
        // join
        System.out.println(StringUtils.join("1", "2", "3", "4"));
        System.out.println(StringUtils.join(new String[]{"1", "2", "3", "4"}, ','));
        // repeat
        System.out.println(StringUtils.repeat("h", 10));
    }
}
