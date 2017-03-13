package com.bigwis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/10.
 */
public class StringUtils {

    public static final String numberChar = "0123456789";

    protected static final Pattern VAR_PATTERN = Pattern.compile("(\\$\\{)[^}]{1,}(\\})");

    /**
     * 驼峰命名转换
     */
    private static final char SEPARATOR = '_';

    public static Integer toInteger(Object srcStr, Integer defaultValue) {
        try {
            if (srcStr != null && StringUtils.isInt(srcStr)) {
                String s = srcStr.toString().replaceAll("(\\s)", "");
                return s.length() > 0 ? Integer.valueOf(s) : defaultValue;
            }
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static boolean isInt(Object srcStr) {
        if (srcStr == null) {
            return false;
        }
        String s = srcStr.toString().replaceAll("(\\s)", "");
        Pattern p = Pattern.compile("([-]?[\\d]+)");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean isEmpty(Object srcStr) {
        return nvl(srcStr, "").trim().length() == 0
                || nvl(srcStr, "").equals("null");
    }

    public static String nvl(Object src, String alt) {
        if (src == null) {
            return alt;
        } else {
            return StringUtils.nvl(src.toString(), alt);
        }
    }

    /**
     * 空字符串处理，空字符串返回指定字符串
     * @param srcStr
     * @param objStr
     * @return
     */
    public static String nvl(String srcStr, String objStr) {
        if (srcStr == null || 0 == srcStr.trim().length()
                || "null".equalsIgnoreCase(srcStr.trim())) {
            return objStr;
        } else {
            return srcStr;
        }
    }

    /**
     * 将驼峰格式字符串转换为下划线格式字符串
     * @param s
     * @return
     */
    public static String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
        if (s.indexOf(".") != -1) {
            s = s.split("\\.")[s.split("\\.").length-1];
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
